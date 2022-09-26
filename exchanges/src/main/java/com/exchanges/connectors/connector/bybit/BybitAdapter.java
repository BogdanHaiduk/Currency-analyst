package com.exchanges.connectors.connector.bybit;

import com.exchanges.connectors.connector.bybit.rest.dto.BybitOrderBook;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitTrade;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitWSOrderBook;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Side;
import com.exchanges.dto.Trade;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BybitAdapter {
    static String EXCHANGE_NAME = "bybit";

    public static String toCurrencyPair(CurrencyPair currencyPair) {
        return currencyPair.getBaseCurrency()
                .concat(currencyPair.getQuoteCurrency());
    }

    public static OrderBook toOrderBook(BybitWSOrderBook bybitWSOrderBook, CurrencyPair currencyPair) {
        if (bybitWSOrderBook != null && bybitWSOrderBook.getData() != null) {
            BybitWSOrderBook bybitOrderBook = bybitWSOrderBook.getData();

            return OrderBook.builder(EXCHANGE_NAME, bybitOrderBook.getTimestamp())
                    .currencyPair(currencyPair)
                    .asks(createAskAndBid(bybitOrderBook.getAsks()))
                    .bids(createAskAndBid(bybitOrderBook.getBids()))
                    .build();
        }

        return OrderBook.builder(EXCHANGE_NAME, new Date().getTime()).build();
    }

    private static Map<BigDecimal, BigDecimal> createAskAndBid(List<BigDecimal[]> priceAndAmount) {
        if (priceAndAmount != null) {
            Map<BigDecimal, BigDecimal> askOrBid = new HashMap<>();
            priceAndAmount.forEach(orderBid -> askOrBid.put(orderBid[0], orderBid[1]));

            return askOrBid;
        }

        return Collections.emptyMap();
    }

    public static Side toSide(int side) {
        return side == 0 ? Side.BID : Side.ASK;
    }

    public static List<Trade> toListTrades(List<BybitTrade> trades, CurrencyPair currencyPair) {
        if (trades != null)
            return trades.stream()
                    .map(bybitTrade -> Trade.builder()
                            .currencyPair(currencyPair)
                            .exchangeName(EXCHANGE_NAME)
                            .side(toSide(bybitTrade.getIsBuyerMaker()))
                            .size(bybitTrade.getQty())
                            .timestamp(bybitTrade.getTime())
                            .price(bybitTrade.getPrice())
                            .build())
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }

    public static OrderBook toOrderBook(BybitOrderBook bybitOrderBook, CurrencyPair currencyPair) {
        if (bybitOrderBook != null)
            return OrderBook.builder(EXCHANGE_NAME, bybitOrderBook.getTime())
                    .currencyPair(currencyPair)
                    .asks(createAskAndBid(bybitOrderBook.getAsks()))
                    .bids(createAskAndBid(bybitOrderBook.getBids()))
                    .build();

        return OrderBook.builder(EXCHANGE_NAME, new Date().getTime()).build();
    }
}
