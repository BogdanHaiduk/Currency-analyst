package com.exchanges.connectors.connector.bybit.ws.connection;

import com.exchanges.connectors.connector.bybit.ws.handler.BybitWsHandlerExchange;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestBybitWsConnectionExchange {
    @InjectMocks
    BybitWsConnectionExchange bybitWsConnectionExchange;

    @Mock
    BybitWsHandlerExchange wsHandlerExchange;

    @Test
    public void getPathWS_correctPathConnectWs_pathWS() {
        assertEquals("wss://stream.bybit.com/spot/public/v3", bybitWsConnectionExchange.getPathWS());
    }

    @Test
    public void getTimePing_correctIntervalTimePing_timePing() {
        assertEquals(15, bybitWsConnectionExchange.getTimePing());
    }

    @Test
    public void getUnitTimePing_correctUnitTimePing_TimeUnitPing() {
        assertEquals(TimeUnit.SECONDS, bybitWsConnectionExchange.getUnitTimePing());
    }

    @Test
    public void getMessagePing_correctMessagePing_MessagePing() {
        assertEquals("{\"op\": \"ping\"};", bybitWsConnectionExchange.getMessagePing());
    }

    @Test
    public void connect_successfulWSConnection() {
        bybitWsConnectionExchange.connect();
        assertNotNull(wsHandlerExchange.getWebSocketSessionConnector());
    }
}
