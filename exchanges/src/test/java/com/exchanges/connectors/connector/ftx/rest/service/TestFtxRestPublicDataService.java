package com.exchanges.connectors.connector.ftx.rest.service;

import com.exchanges.connectors.connector.ftx.rest.connection.FtxRestConnectionExchange;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxMarket;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import com.exchanges.util.TestUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestFtxRestPublicDataService {
    static String EXCHANGE_NAME = "ftx";
    static String CURRENCY_PAIR = "BTC-USDT";
    static String FILE_PATH_REST = "/connectors/connector/ftx/rest/";

    @InjectMocks
    FtxRestPublicDataService restPublicDataService;
    @Mock
    FtxRestConnectionExchange ftxRestConnectionExchange;

    @Test
    public void getOrderBook_getOrderBookImitationFromFTX_OrderBook() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(ftxRestConnectionExchange.getOrderBook(any()))
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "ftxOrderBook.json", FtxOrderBookRest.class));

        OrderBook orderBookActual =
                restPublicDataService.getOrderBook(currencyPair);

        assertEquals(EXCHANGE_NAME, orderBookActual.getExchangeName());
        assertEquals(currencyPair, orderBookActual.getCurrencyPair());

        Map<BigDecimal, BigDecimal> asks = orderBookActual.getAsks();
        Map<BigDecimal, BigDecimal> bids = orderBookActual.getBids();

        assertTrue(asks != null && asks.size() == 2);
        assertTrue(bids != null && bids.size() == 2);
        assertEquals(0, bids.get(new BigDecimal("19017.0")).compareTo(new BigDecimal("1.4402")));
        assertEquals(0, asks.get(new BigDecimal("19018.0")).compareTo(new BigDecimal("0.7873")));
    }

    @Test
    public void getCurrencyPairs_getImitationCurrencyPairsBySpotMarketFromFTX_ListCurrencyPair() {
        when(ftxRestConnectionExchange.getMarkets())
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "ftxMarkets.json", FtxMarket.class));

        List<CurrencyPair> currencyPairsBySpot = restPublicDataService.getCurrencyPairsBySpotMarket();

        assertTrue(currencyPairsBySpot != null && currencyPairsBySpot.size() == 3);
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("BTC", "USDT")));
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("TRX", "USDT")));
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("ALGO", "USD")));
    }

    @Test
    public void getTrades_getImitationTradesFromFTXWithoutTimeParams_ListTrade() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(ftxRestConnectionExchange.getTrades(any(), anyLong(), anyLong()))
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "ftxTrades.json", FtxTradeRest.class));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
    }

    @Test
    public void getTrades_getImitationTradesFromFTXWithStartTimeParams_ListTrade() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(ftxRestConnectionExchange.getTrades(any(), anyLong(), anyLong()))
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "ftxTrades.json", FtxTradeRest.class));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair, 1663934808657L);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
    }

    @Test
    public void getTrades_getImitationTradesFromFTXWithStartAndEndTimeParams_ListTrade() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(ftxRestConnectionExchange.getTrades(any(), anyLong(), anyLong()))
                .thenReturn(TestUtil.fromJson(FILE_PATH_REST, "ftxTrades.json", FtxTradeRest.class));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair, 1663934808657L);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
    }
}
