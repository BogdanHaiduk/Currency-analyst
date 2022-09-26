package com.exchanges.connectors.connector.bybit;

import com.exchanges.connectors.connector.bybit.rest.dto.BybitOrderBook;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitTrade;
import com.exchanges.connectors.connector.bybit.ws.dto.BybitWSOrderBook;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Side;
import com.exchanges.dto.Trade;
import com.exchanges.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestBybitAdapter {
    static String EXCHANGE_NAME = "bybit";

    static String CURRENCY_PAIR = "BTC-USDT";
    static String CURRENCY_PAIR_BYBIT_FORMAT = "BTCUSDT";

    static String FILE_PATH_REST = "/connectors/connector/bybit/rest/";
    static String FILE_PATH_WS = "/connectors/connector/bybit/ws/";


    @Test
    public void toCurrencyPair_adaptingCurrencyPairToBybitFormat_CurrencyPairBybit() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        assertEquals(CURRENCY_PAIR_BYBIT_FORMAT, BybitAdapter.toCurrencyPair(currencyPair));
    }

    @Test
    public void toSide_adaptingSideFromBybitFormat_Side() {
        assertEquals(Side.BID, BybitAdapter.toSide(0));
        assertEquals(Side.ASK, BybitAdapter.toSide(1));
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromBybitFormatWS_OrderBook() {
        BybitWSOrderBook bybitWSOrderBook = TestUtil.fromJson(
                FILE_PATH_WS,
                "wsOrderBook.json",
                BybitWSOrderBook.class);

        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);
        OrderBook orderBook = BybitAdapter.toOrderBook(bybitWSOrderBook, currencyPair);

        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(currencyPair, orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 3);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 3);
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromNullWS_OrderBook() {
        BybitWSOrderBook bybitWSOrderBook = null;
        OrderBook orderBook = BybitAdapter.toOrderBook(bybitWSOrderBook, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getBids() != null && orderBook.getBids().isEmpty());
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromBybitFormatREST_OrderBook() {
        BybitOrderBook bybitOrderBook = TestUtil.fromJson(
                FILE_PATH_REST,
                "bybitOrderBook.json",
                BybitOrderBook.class);
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);
        OrderBook orderBook = BybitAdapter.toOrderBook(bybitOrderBook, currencyPair);

        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(currencyPair, orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 2);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 2);
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromNullREST_OrderBook() {
        BybitOrderBook bybitRestOrderBook = null;
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);
        OrderBook orderBook = BybitAdapter.toOrderBook(bybitRestOrderBook, currencyPair);

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getBids() != null && orderBook.getBids().isEmpty());
    }

    @Test
    public void toListTrades_adaptingTradesFromBybitFormat_ListTrade() {
        List<BybitTrade> bybitRestTrade = TestUtil.fromJson(
                FILE_PATH_REST,
                "bybitTrade.json",
                new TypeReference<List<BybitTrade>>() {
                });
        List<Trade> trades = BybitAdapter.toListTrades(bybitRestTrade, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(trades);
        assertFalse(trades.isEmpty());
        assertEquals(EXCHANGE_NAME, trades.get(0).getExchangeName());
    }

    @Test
    public void toListTrades_adaptingTradesFromNull_ListTrade() {
        List<Trade> trades = BybitAdapter.toListTrades(null, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }
}
