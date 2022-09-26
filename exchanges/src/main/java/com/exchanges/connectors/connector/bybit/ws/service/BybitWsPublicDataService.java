package com.exchanges.connectors.connector.bybit.ws.service;

import com.exchanges.connectors.connector.bybit.BybitAdapter;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitChannel;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitMessageSubscribe;
import com.exchanges.connectors.core.ws.connection.WsConnectionExchange;
import com.exchanges.connectors.core.ws.service.WsPublicDataService;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.EventConnector;
import com.exchanges.dto.Topic;
import com.exchanges.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitWsPublicDataService implements WsPublicDataService {
    final static int DEPTH_ORDER_BOOK = 20;
    final static String SUBSCRIBE_OP = "subscribe";
    final static String UNSUBSCRIBE_OP = "unsubscribe";
    final WsConnectionExchange wsConnectionExchange;

    @Autowired
    public BybitWsPublicDataService(
            @Qualifier("bybitWsConnectionExchange") WsConnectionExchange wsConnectionExchange) {
        this.wsConnectionExchange = wsConnectionExchange;
    }

    private String createChannel(CurrencyPair currencyPair) {
        return String.format(
                "%s.%s.%s",
                BybitChannel.ORDER_BOOK.getChannelName(),
                DEPTH_ORDER_BOOK,
                BybitAdapter.toCurrencyPair(currencyPair)
        );
    }

    @Override
    public void subscribeOrderBook(CurrencyPair currencyPair) {
        BybitMessageSubscribe bybitMessageSubscribe =
                new BybitMessageSubscribe(SUBSCRIBE_OP, new String[]{createChannel(currencyPair)});

        wsConnectionExchange.subscribe(
                new EventConnector(
                        currencyPair,
                        Topic.ORDER_BOOK,
                        JsonUtils.toJson(bybitMessageSubscribe)
                ));
    }

    @Override
    public void unsubscribeOrderBook(CurrencyPair currencyPair) {
        BybitMessageSubscribe bybitMessageSubscribe =
                new BybitMessageSubscribe(UNSUBSCRIBE_OP, new String[]{createChannel(currencyPair)});

        wsConnectionExchange.unsubscribe(
                new EventConnector(
                        currencyPair,
                        Topic.ORDER_BOOK,
                        JsonUtils.toJson(bybitMessageSubscribe)
                ));
    }
}
