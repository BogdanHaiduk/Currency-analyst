package com.exchanges.connectors.connector.bybit.ws.handler;

import com.exchanges.connectors.connector.bybit.BybitAdapter;
import com.exchanges.connectors.connector.bybit.rest.connection.BybitRestConnectionExchange;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitChannel;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitWSOrderBook;
import com.exchanges.connectors.core.ws.handler.HandlerMessage;
import com.exchanges.connectors.core.ws.handler.orderbook.HandlerOrderBook;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitWSHandlerMessage extends HandlerMessage {
    static final String TOPIC_CHANNEL_ORDERBOOK = "\"topic\":\"orderbook.";
    final BybitRestConnectionExchange restConnectionExchange;
    List<BybitMarket> bybitSpotMarket;

    public BybitWSHandlerMessage(@Qualifier("handlerOrderBookImpl") HandlerOrderBook handlerOrderBook,
                                 BybitRestConnectionExchange restConnectionExchange) {
        super(handlerOrderBook);
        this.restConnectionExchange = restConnectionExchange;
    }

    @Override
    public String checkChannel(String jsonPayload) {
        if (jsonPayload.contains(TOPIC_CHANNEL_ORDERBOOK))
            return BybitChannel.ORDER_BOOK.getChannelName();

        return "";
    }

    @Override
    public String handleMessageByChannel(String channel, String jsonPayload) {
        if (BybitChannel.ORDER_BOOK.getChannelName().equals(channel))
            return handleMessageBybitOrderBook(jsonPayload);

        return "";
    }

    private String handleMessageBybitOrderBook(String jsonPayload) {
        BybitWSOrderBook bybitWSOrderBook = JsonUtils.fromJson(jsonPayload, BybitWSOrderBook.class);
        CurrencyPair currencyPair = getBybitRestMarketList()
                .stream()
                .filter(bybitMarket -> bybitMarket.getName().equals(bybitWSOrderBook.getData().getMarket()))
                .map(bybitMarket -> new CurrencyPair(bybitMarket.getBaseCoin(), bybitMarket.getQuoteCoin()))
                .findFirst()
                .orElse(null);

        OrderBook orderBook = BybitAdapter.toOrderBook(bybitWSOrderBook, currencyPair);
        orderBook = getHandlerOrderBook().cutOrderBook(orderBook);

        return (JsonUtils.toJson(orderBook));
    }

    private List<BybitMarket> getBybitRestMarketList() {
        if (bybitSpotMarket == null)
            bybitSpotMarket = restConnectionExchange.getMarkets();

        return bybitSpotMarket;
    }
}
