package com.exchanges.connectors.core.ws.handler.orderbook;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandlerOrderBookImpl implements HandlerOrderBook {
    final static int DEPTH_ORDER_BOOK = 5;
    final Map<String, Map<CurrencyPair, OrderBook>> orderBookAllExchanges = new ConcurrentHashMap<>();

    public OrderBook setSnapshotOrderBook(OrderBook orderBook) {
        Map<CurrencyPair, OrderBook> orderBooksExchange = orderBookAllExchanges.get(orderBook.getExchangeName());
        orderBook = cutOrderBook(orderBook);

        if (orderBooksExchange != null) {
            orderBooksExchange.put(orderBook.getCurrencyPair(), orderBook);
        } else {
            OrderBook finalOrderBook = orderBook;
            orderBooksExchange = new ConcurrentHashMap<CurrencyPair, OrderBook>() {{
                put(finalOrderBook.getCurrencyPair(), finalOrderBook);
            }};
        }
        orderBookAllExchanges.put(orderBook.getExchangeName(), orderBooksExchange);

        return orderBook;
    }

    public OrderBook cutOrderBook(OrderBook orderBook) {
        Map<BigDecimal, BigDecimal> asks = orderBook.getAsks().entrySet()
                .stream().limit(DEPTH_ORDER_BOOK)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<BigDecimal, BigDecimal> bids = orderBook.getBids().entrySet()
                .stream().limit(DEPTH_ORDER_BOOK)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return OrderBook.builder(orderBook.getExchangeName(), orderBook.getTimestamp())
                .currencyPair(orderBook.getCurrencyPair())
                .asks(asks)
                .bids(bids)
                .build();
    }

    public OrderBook update(OrderBook updateOrderBook) {
        orderBookAllExchanges.entrySet()
                .stream()
                .filter(orderBookAllExchangesEntry -> orderBookAllExchangesEntry.getKey().equals(updateOrderBook.getExchangeName()))
                .map(Map.Entry::getValue)
                .filter(exchangeOrderBook -> exchangeOrderBook.containsKey(updateOrderBook.getCurrencyPair()))
                .forEach(exchangeOrderBook -> {
                    OrderBook exchangeOrderBookByCurrencyPair = exchangeOrderBook.get(updateOrderBook.getCurrencyPair());
                    updateAskAndBid(updateOrderBook, exchangeOrderBookByCurrencyPair);
                    exchangeOrderBookByCurrencyPair.setTimestamp(updateOrderBook.getTimestamp());
                    exchangeOrderBookByCurrencyPair = cutOrderBook(exchangeOrderBookByCurrencyPair);
                    exchangeOrderBook.put(exchangeOrderBookByCurrencyPair.getCurrencyPair(), exchangeOrderBookByCurrencyPair);
                });

        return orderBookAllExchanges.get(updateOrderBook.getExchangeName()).get(updateOrderBook.getCurrencyPair());
    }

    private void updateAskAndBid(OrderBook updateOrderBook, OrderBook exchangeOrderBookByCurrencyPair) {
        updateOrderBook.getAsks().forEach((price, amount) -> {
            if (0 == amount.compareTo(BigDecimal.ZERO))
                exchangeOrderBookByCurrencyPair
                        .getAsks()
                        .remove(price);
            else
                exchangeOrderBookByCurrencyPair
                        .getAsks()
                        .put(price, amount);
        });

        updateOrderBook.getBids().forEach((price, amount) -> {
            if (0 == amount.compareTo(BigDecimal.ZERO))
                exchangeOrderBookByCurrencyPair
                        .getBids()
                        .remove(price);
            else
                exchangeOrderBookByCurrencyPair
                        .getBids()
                        .put(price, amount);
        });
    }
}