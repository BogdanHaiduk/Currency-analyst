package com.exchanges.connectors.connector.bybit.ws.handler;

import com.exchanges.connectors.connector.bybit.rest.connection.BybitRestConnectionExchange;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.core.ws.handler.orderbook.HandlerOrderBookImpl;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.util.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestBybitHandlerMessage {
    static String CURRENCY_PAIR = "BTC-USDT";
    static String EXCHANGE_NAME = "bybit";
    static final String CHANNEL_ORDERBOOK = "orderbook";

    static final String FILE_PATH_WS = "/connectors/connector/bybit/ws/";
    static final String FILE_PATH_REST = "/connectors/connector/bybit/rest/";

    @InjectMocks
    BybitWSHandlerMessage bybitHandlerMessage;

    @Spy
    HandlerOrderBookImpl handlerOrderBook;

    @Mock
    BybitRestConnectionExchange bybitRestConnectionExchange;

    @Test
    public void checkChannel_handlingMessageWithOrderBookChanel_NameChannel() {
        String bybitOrderBookJson = TestUtil.readJson(FILE_PATH_WS, "wsOrderBook.json");
        String chanel = bybitHandlerMessage.checkChannel(bybitOrderBookJson);

        assertEquals(CHANNEL_ORDERBOOK, chanel);
    }

    @Test
    public void handleMessageByChannel_handlingMessageByChannelOrderBookWithTypePartial_OrderBook() throws JsonProcessingException {
        String bybitOrderBookJson = TestUtil.readJson(FILE_PATH_WS, "wsOrderBook.json");

        when(bybitRestConnectionExchange.getMarkets())
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "bybitMarket.json", new TypeReference<List<BybitMarket>>() {
                }));

        String orderBookJson = bybitHandlerMessage.handleMessageByChannel(CHANNEL_ORDERBOOK, bybitOrderBookJson);
        OrderBook orderBook = new ObjectMapper().readValue(orderBookJson, OrderBook.class);

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(new CurrencyPair(CURRENCY_PAIR), orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 3);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 3);
    }
}
