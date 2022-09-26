package com.exchanges.connectors.connector.ftx.ws.handler;

import com.exchanges.connectors.core.ws.handler.orderbook.HandlerOrderBookImpl;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.util.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestFtxHandlerMessage {
    static String EXCHANGE_NAME = "ftx";
    static String CURRENCY_PAIR = "BTC-USDT";
    static final String CHANNEL_ORDERBOOK = "orderbook";
    static final String FILE_PATH_WS = "/connectors/connector/ftx/ws/";

    final FtxHandlerMessage ftxHandlerMessage = new FtxHandlerMessage(new HandlerOrderBookImpl());

    @Test
    public void checkChannel_handlingMessageWithOrderBookChanel_NameChannel() {
        String ftxOrderBookSnapshotJson = TestUtil.readJson(FILE_PATH_WS, "ftxWSOrderBookSnapshot.json");
        String chanel = ftxHandlerMessage.checkChannel(ftxOrderBookSnapshotJson);

        assertEquals(CHANNEL_ORDERBOOK, chanel);
    }

    @Test
    public void handleMessageByChannel_handlingMessageByChannelOrderBookWithTypePartial_OrderBook() throws JsonProcessingException {
        String ftxOrderBookSnapshotJson = TestUtil.readJson(FILE_PATH_WS, "ftxWSOrderBookSnapshot.json");
        String orderBookJson = ftxHandlerMessage.handleMessageByChannel(CHANNEL_ORDERBOOK, ftxOrderBookSnapshotJson);
        OrderBook orderBook = new ObjectMapper().readValue(orderBookJson, OrderBook.class);

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(new CurrencyPair(CURRENCY_PAIR), orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 5);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 5);
    }

    @Test
    public void handleMessageByChannel_handlingMessageByChannelOrderBookWithTypeUpdate_OrderBook() throws JsonProcessingException {
        String ftxOrderBookSnapshotJson = TestUtil.readJson(FILE_PATH_WS, "ftxWSOrderBookSnapshot.json");
        String ftxOrderBookUpdateJson = TestUtil.readJson(FILE_PATH_WS, "ftxWSOrderBookUpdate.json");

        ftxHandlerMessage.handleMessageByChannel(CHANNEL_ORDERBOOK, ftxOrderBookSnapshotJson);

        String orderBookJson = ftxHandlerMessage.handleMessageByChannel(CHANNEL_ORDERBOOK, ftxOrderBookUpdateJson);

        OrderBook orderBook = new ObjectMapper().readValue(orderBookJson, OrderBook.class);

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(new CurrencyPair(CURRENCY_PAIR), orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 4);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 4);
    }
}
