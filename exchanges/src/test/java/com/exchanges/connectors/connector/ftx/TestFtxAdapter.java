package com.exchanges.connectors.connector.ftx;

import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import com.exchanges.connectors.connector.ftx.ws.dto.orderBook.FtxWsOrderBook;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import com.exchanges.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFtxAdapter {
    static String EXCHANGE_NAME = "ftx";
    static String CURRENCY_PAIR = "BTC-USDT";
    static String CURRENCY_PAIR_FTX_FORMAT = "BTC/USDT";
    static String FILE_PATH_REST = "/connectors/connector/ftx/rest/";
    static String FILE_PATH_WS = "/connectors/connector/ftx/ws/";


    @Test
    public void toCurrencyPairFtx_adaptingCurrencyPairToFtxFormat_CurrencyPairFtx() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        assertEquals(CURRENCY_PAIR_FTX_FORMAT, FtxAdapter.toCurrencyPairFtx(currencyPair));
    }

    @Test
    public void toCurrencyPair_adaptingCurrencyPairFromFtxFormat_CurrencyPair() {
        assertEquals(new CurrencyPair(CURRENCY_PAIR), FtxAdapter.toCurrencyPair(CURRENCY_PAIR_FTX_FORMAT));
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromFtxFormatWS_OrderBook() {
        FtxWsOrderBook ftxWsOrderBook = TestUtil.fromJson(
                FILE_PATH_WS,
                "ftxWSOrderBookUpdate.json",
                FtxWsOrderBook.class);

        OrderBook orderBook = FtxAdapter.toOrderBook(ftxWsOrderBook);

        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(new CurrencyPair(CURRENCY_PAIR), orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 2);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 2);
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromNullWS_OrderBook() {
        OrderBook orderBook = FtxAdapter.toOrderBook(null);

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getBids() != null && orderBook.getBids().isEmpty());
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromFtxFormatREST_OrderBook() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);
        FtxOrderBookRest ftxOrderBookRest = TestUtil.fromJson(
                FILE_PATH_REST,
                "ftxOrderBook.json",
                FtxOrderBookRest.class);

        OrderBook orderBook = FtxAdapter.toOrderBook(ftxOrderBookRest, currencyPair);

        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertEquals(currencyPair, orderBook.getCurrencyPair());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().size() == 2);
        assertTrue(orderBook.getBids() != null && orderBook.getBids().size() == 2);
    }

    @Test
    public void toOrderBook_adaptingOrderBookFromNullREST_OrderBook() {
        OrderBook orderBook = FtxAdapter.toOrderBook(null, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(orderBook);
        assertEquals(EXCHANGE_NAME, orderBook.getExchangeName());
        assertTrue(orderBook.getAsks() != null && orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getBids() != null && orderBook.getBids().isEmpty());
    }

    @Test
    public void toListTrade_adaptingTradesFromFtxFormat_ListTrade() {
        FtxTradeRest ftxTradeRest = TestUtil.fromJson(
                FILE_PATH_REST,
                "ftxTrades.json",
                FtxTradeRest.class);
        List<Trade> trades = FtxAdapter.toListTrade(ftxTradeRest, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(trades);
        assertFalse(trades.isEmpty());
        assertEquals(EXCHANGE_NAME, trades.get(0).getExchangeName());
    }

    @Test
    public void toListTrade_adaptingTradesFromNull_ListTrade() {
        List<Trade> trades = FtxAdapter.toListTrade(null, new CurrencyPair(CURRENCY_PAIR));

        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    public void toTimestamp_adaptingTimestampFromFtxFormat_Date() {
        Date date = FtxAdapter.toTimestamp("2022-09-23T12:05:44.463917+00:00");

        assertEquals(1663934744463L, date.getTime());
    }

    @Test
    public void toTimestamp_adaptingTimestampFromNull_Date() {
        assertNotNull(FtxAdapter.toTimestamp(null));
    }
}