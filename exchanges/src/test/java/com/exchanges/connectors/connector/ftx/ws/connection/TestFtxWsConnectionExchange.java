package com.exchanges.connectors.connector.ftx.ws.connection;

import com.exchanges.connectors.connector.ftx.ws.handler.FtxWSHandlerExchange;
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
public class TestFtxWsConnectionExchange {
    @InjectMocks
    FtxWsConnectionExchange ftxWsConnectionExchange;

    @Mock
    FtxWSHandlerExchange wsHandlerExchange;

    @Test
    public void getPathWS_correctPathConnectWs_pathWS() {
        assertEquals("wss://ftx.com/ws/", ftxWsConnectionExchange.getPathWS());
    }

    @Test
    public void getTimePing_correctIntervalTimePing_timePing() {
        assertEquals(10, ftxWsConnectionExchange.getTimePing());
    }

    @Test
    public void getUnitTimePing_correctUnitTimePing_TimeUnitPing() {
        assertEquals(TimeUnit.SECONDS, ftxWsConnectionExchange.getUnitTimePing());
    }

    @Test
    public void getMessagePing_correctMessagePing_MessagePing() {
        assertEquals("{\"op\": \"ping\"}", ftxWsConnectionExchange.getMessagePing());
    }

    @Test
    public void connect_successfulWSConnection() {
        ftxWsConnectionExchange.connect();
        assertNotNull(wsHandlerExchange.getWebSocketSessionConnector());
    }
}
