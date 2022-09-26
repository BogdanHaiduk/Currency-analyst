package com.exchanges.connectors.connector.bybit.ws.connection;

import com.exchanges.connectors.core.ws.connection.WsConnectionExchangeImpl;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BybitWsConnectionExchange extends WsConnectionExchangeImpl {
    public BybitWsConnectionExchange(@Qualifier("bybitWsHandlerExchange") WsHandlerExchange wsHandlerExchange) {
        super(wsHandlerExchange);
    }

    @Override
    public void connect() {
        super.connect();
    }

    @Override
    public String getPathWS() {
        return "wss://stream.bybit.com/spot/public/v3";
    }

    @Override
    protected int getTimePing() {
        return 15;
    }

    @Override
    protected TimeUnit getUnitTimePing() {
        return TimeUnit.SECONDS;
    }

    @Override
    protected String getMessagePing() {
        return "{\"op\": \"ping\"};";
    }
}
