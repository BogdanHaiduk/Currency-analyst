package com.exchanges.connectors.core.ws.handler.orderbook;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestHandlerOrderBookImpl {
    static OrderBook orderBookSnapshot;
    HandlerOrderBook handlerOrderBook;

    @BeforeEach
    public void initEach() {
        handlerOrderBook = new HandlerOrderBookImpl();
    }

    @BeforeAll
    public static void initAll() {
        Map<BigDecimal, BigDecimal> asks = new HashMap<BigDecimal, BigDecimal>() {{
            put(new BigDecimal("19142.0"), new BigDecimal("0.7831"));
            put(new BigDecimal("19143.0"), new BigDecimal("1.923"));
            put(new BigDecimal("19139.0"), new BigDecimal("0.8524"));
            put(new BigDecimal("19140.0"), new BigDecimal("0.7088"));
            put(new BigDecimal("19141.0"), new BigDecimal("15.3194"));
            put(new BigDecimal("19129.0"), new BigDecimal("5.24"));
            put(new BigDecimal("19110.0"), new BigDecimal("7.88"));
            put(new BigDecimal("19131.0"), new BigDecimal("1.34"));
        }};

        Map<BigDecimal, BigDecimal> bids = new HashMap<BigDecimal, BigDecimal>() {{
            put(new BigDecimal("19136.0"), new BigDecimal("2.8766"));
            put(new BigDecimal("19135.0"), new BigDecimal("4.4374"));
            put(new BigDecimal("19134.0"), new BigDecimal("3.8756"));
            put(new BigDecimal("19137.0"), new BigDecimal("1.0157"));
            put(new BigDecimal("19138.0"), new BigDecimal("0.8527"));
            put(new BigDecimal("19132.0"), new BigDecimal("3.8756"));
            put(new BigDecimal("19167.0"), new BigDecimal("10.57"));
            put(new BigDecimal("19131.0"), new BigDecimal("4.887"));
        }};

        orderBookSnapshot = OrderBook.builder("binance", 1663839944733L)
                .bids(bids)
                .asks(asks)
                .currencyPair(new CurrencyPair("BTC", "USDT"))
                .build();
    }

    @Test
    public void setSnapshotOrderBook_setFirstSnapshotOrderBook_test() {
        OrderBook orderBook = handlerOrderBook.setSnapshotOrderBook(orderBookSnapshot);

        assertNotNull(orderBook);
        assertEquals("binance", orderBook.getExchangeName());
        assertEquals(1663839944733L, orderBook.getTimestamp());
        assertEquals(new CurrencyPair("BTC", "USDT"), orderBook.getCurrencyPair());
        assertNotNull(orderBook.getAsks());
        assertNotNull(orderBook.getBids());
        assertEquals(5, orderBook.getAsks().size());
        assertEquals(5, orderBook.getBids().size());
    }

    @Test
    public void update_updateOrderBookData_test() {
        handlerOrderBook.setSnapshotOrderBook(orderBookSnapshot);
        OrderBook orderBookUpdate = createOrderBookForUpdate();

        OrderBook orderBookAfterUpdate = handlerOrderBook.update(orderBookUpdate);

        assertNotNull(orderBookAfterUpdate);
        assertEquals("binance", orderBookAfterUpdate.getExchangeName());
        assertEquals(1663839944824L, orderBookAfterUpdate.getTimestamp());
        assertEquals(new CurrencyPair("BTC", "USDT"), orderBookAfterUpdate.getCurrencyPair());
        assertNotNull(orderBookAfterUpdate.getAsks());
        assertNotNull(orderBookAfterUpdate.getBids());
        assertEquals(5, orderBookAfterUpdate.getAsks().size());
        assertEquals(5, orderBookAfterUpdate.getBids().size());

        Map<BigDecimal, BigDecimal> asksOrderBookUpdate = orderBookAfterUpdate.getAsks();
        Map<BigDecimal, BigDecimal> bidsOrderBookUpdate = orderBookAfterUpdate.getBids();

        assertNull(bidsOrderBookUpdate.get(new BigDecimal("19167.0")));
        assertEquals(0, bidsOrderBookUpdate.get(new BigDecimal("19168.0")).compareTo(new BigDecimal("0.0001")));
        assertNull(asksOrderBookUpdate.get(new BigDecimal("19110.0")));
        assertEquals(0, asksOrderBookUpdate.get(new BigDecimal("19111.0")).compareTo(new BigDecimal("0.0021")));
    }

    private OrderBook createOrderBookForUpdate() {
        return OrderBook.builder("binance", 1663839944824L)
                .bids(new HashMap<BigDecimal, BigDecimal>() {{
                    put(new BigDecimal("19167.0"), BigDecimal.ZERO);
                    put(new BigDecimal("19168.0"), new BigDecimal("0.0001"));
                }})
                .asks(new HashMap<BigDecimal, BigDecimal>() {{
                    put(new BigDecimal("19110.0"), BigDecimal.ZERO);
                    put(new BigDecimal("19111.0"), new BigDecimal("0.0021"));
                }})
                .currencyPair(new CurrencyPair("BTC", "USDT"))
                .build();
    }
}
