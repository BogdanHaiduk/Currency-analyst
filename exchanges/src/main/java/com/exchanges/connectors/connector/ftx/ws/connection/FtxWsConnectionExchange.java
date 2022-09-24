package com.exchanges.connectors.connector.ftx.ws.connection;

import com.exchanges.connectors.core.ws.connection.WsConnectionExchangeImpl;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxWsConnectionExchange extends WsConnectionExchangeImpl {
    final static String WS_PATH = "wss://ftx.com/ws/";

    public FtxWsConnectionExchange(@Qualifier("ftxWSHandlerExchange") WsHandlerExchange wsHandlerExchange) {
        super(wsHandlerExchange);
    }

    @Override
    public String getPathWS() {
        return WS_PATH;
    }

    @Override
    protected int getTimePing() {
        return 10;
    }

    @Override
    protected TimeUnit getUnitTimePing() {
        return TimeUnit.SECONDS;
    }

    @Override
    protected String getMessagePing() {
        return "{\"op\": \"ping\"}";
    }
}
