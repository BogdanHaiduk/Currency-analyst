package com.exchanges.connectors.connector.ftx.ws.service;

import com.exchanges.connectors.connector.ftx.ws.dto.FtxChannel;
import com.exchanges.connectors.connector.ftx.ws.dto.FtxEventMessage;
import com.exchanges.dto.EventConnector;
import com.exchanges.dto.Topic;
import com.exchanges.connectors.core.ws.service.WsPublicDataService;
import com.exchanges.connectors.core.ws.connection.WsConnectionExchange;
import com.exchanges.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxWsPublicDataService implements WsPublicDataService {
    final static String SUBSCRIBE_OP = "subscribe";
    final static String UNSUBSCRIBE_OP = "unsubscribe";
    final WsConnectionExchange wsConnectionExchange;

    @Autowired
    public FtxWsPublicDataService(@Qualifier("ftxWsConnectionExchange") WsConnectionExchange wsConnectionExchange) {
        this.wsConnectionExchange = wsConnectionExchange;
    }

    @Override
    public void subscribeOrderBook(String currencyPair) {
        FtxEventMessage ftxEventMessage =
                new FtxEventMessage(SUBSCRIBE_OP, FtxChannel.ORDER_BOOK.getName(), currencyPair);

        wsConnectionExchange.subscribe(
                new EventConnector(
                        currencyPair,
                        Topic.ORDER_BOOK,
                        JsonUtils.toJson(ftxEventMessage)
                ));
    }

    @Override
    public void unsubscribeOrderBook(String currencyPair) {
        FtxEventMessage ftxEventMessage =
                new FtxEventMessage(UNSUBSCRIBE_OP, FtxChannel.ORDER_BOOK.getName(), currencyPair);

        wsConnectionExchange.unsubscribe(
                new EventConnector(
                        currencyPair,
                        Topic.ORDER_BOOK,
                        JsonUtils.toJson(ftxEventMessage)
                )
        );
    }
}
