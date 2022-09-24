package com.exchanges.util;

import com.exchanges.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestJsonUtils {
    static final Map<BigDecimal, BigDecimal> asks = new HashMap<>();
    static final Map<BigDecimal, BigDecimal> bids = new HashMap<>();
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        asks.put(new BigDecimal("19142.0"), new BigDecimal("0.7831"));
        asks.put(new BigDecimal("19143.0"), new BigDecimal("1.923"));
        asks.put(new BigDecimal("19139.0"), new BigDecimal("0.8524"));
        asks.put(new BigDecimal("19140.0"), new BigDecimal("0.7088"));
        asks.put(new BigDecimal("19141.0"), new BigDecimal("15.3194"));

        bids.put(new BigDecimal("19136.0"), new BigDecimal("2.8766"));
        bids.put(new BigDecimal("19135.0"), new BigDecimal("4.4374"));
        bids.put(new BigDecimal("19134.0"), new BigDecimal("3.8756"));
        bids.put(new BigDecimal("19137.0"), new BigDecimal("1.0157"));
        bids.put(new BigDecimal("19138.0"), new BigDecimal("0.8527"));
    }

    @Test
    public void toJson_orderBookObjectToJson_test() throws IOException {
        OrderBook orderBook = OrderBook.builder("binance", 1663839944733L)
                .currencyPair(new CurrencyPair("BTC", "USDT"))
                .asks(asks)
                .bids(bids)
                .build();

        JsonNode jsonNode = objectMapper.readTree(new ClassPathResource("com/exchanges/util/orderBook.json").getInputStream());

        assertEquals(jsonNode.toString(), JsonUtils.toJson(orderBook));
    }

    @Test
    public void fromJson_messageEventJsonToObject_test() throws IOException {
        MessageEvent messageEvent = new MessageEvent(
                "binance",
                new CurrencyPair("BTC-USDT"),
                TypeEvent.SUBSCRIBE,
                Topic.ORDER_BOOK);
        JsonNode jsonNode = objectMapper.readTree(new ClassPathResource("com/exchanges/util/messageEvent.json").getInputStream());

        assertEquals(messageEvent, JsonUtils.fromJson(jsonNode.toString(), MessageEvent.class));
    }
}
