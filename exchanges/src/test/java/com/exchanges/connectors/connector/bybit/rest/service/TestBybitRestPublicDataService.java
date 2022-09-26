package com.exchanges.connectors.connector.bybit.rest.service;

import com.exchanges.connectors.connector.bybit.rest.connection.BybitRestConnectionExchange;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitOrderBook;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitTrade;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import com.exchanges.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestBybitRestPublicDataService {
    static final String PATH = "/connectors/connector/bybit/rest/";
    static String EXCHANGE_NAME = "bybit";
    static String CURRENCY_PAIR = "BTC-USDT";

    @InjectMocks
    BybitRestPublicDataService restPublicDataService;
    @Mock
    BybitRestConnectionExchange bybitRestConnectionExchange;

    @Test
    public void getOrderBook_getOrderBookImitationFromBybit_OrderBook() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(bybitRestConnectionExchange.getOrderBook(any()))
                .thenReturn(TestUtil.fromJson(PATH, "bybitOrderBook.json", BybitOrderBook.class));

        OrderBook orderBookActual = restPublicDataService.getOrderBook(currencyPair);

        assertEquals(EXCHANGE_NAME, orderBookActual.getExchangeName());
        assertEquals(currencyPair, orderBookActual.getCurrencyPair());

        Map<BigDecimal, BigDecimal> asks = orderBookActual.getAsks();
        Map<BigDecimal, BigDecimal> bids = orderBookActual.getBids();

        assertTrue(asks != null && asks.size() == 2);
        assertTrue(bids != null && bids.size() == 2);
        assertEquals(0, bids.get(new BigDecimal("19082.49")).compareTo(new BigDecimal("0.988092")));
        assertEquals(0, asks.get(new BigDecimal("19086.23")).compareTo(new BigDecimal("0.966549")));
    }

    @Test
    public void getCurrencyPairsBySpotMarket_getImitationCurrencyPairsBySpotMarketFromBybit_ListCurrencyPair() {
        when(bybitRestConnectionExchange.getMarkets())
                .thenReturn(TestUtil.fromJson(PATH, "bybitMarket.json", new TypeReference<List<BybitMarket>>() {
                }));

        List<CurrencyPair> currencyPairsBySpot = restPublicDataService.getCurrencyPairsBySpotMarket();

        assertTrue(currencyPairsBySpot != null && currencyPairsBySpot.size() == 3);
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("BTC", "USDT")));
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("ETH", "USDT")));
        assertTrue(currencyPairsBySpot.contains(new CurrencyPair("XRP", "USDT")));
    }

    @Test
    public void getTrades_getImitationTradesFromBybitWithoutTimeParams_ListTrade() {
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(bybitRestConnectionExchange.getTrades(any()))
                .thenReturn(TestUtil.fromJson(PATH, "bybitTrade.json", new TypeReference<List<BybitTrade>>() {
                }));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
    }

    @Test
    public void getTrades_getImitationTradesFromBybitWithStartTimeParams_ListTrade() {
        long startTime = 1663934808657L;
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(bybitRestConnectionExchange.getTrades(any()))
                .thenReturn(TestUtil.fromJson(PATH, "bybitTrade.json", new TypeReference<List<BybitTrade>>() {
                }));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair, startTime);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
        assertTrue(trades.stream().allMatch(trade -> trade.getTimestamp() >= 1663934808657L));
    }

    @Test
    public void getTrades_getImitationTradesFromBybitWithStartAndEndTimeParams_ListTrade() {
        long startTime = 1663934808657L;
        long endTime = 1664187709091L;
        CurrencyPair currencyPair = new CurrencyPair(CURRENCY_PAIR);

        when(bybitRestConnectionExchange.getTrades(any()))
                .thenReturn(TestUtil.fromJson(PATH, "bybitTrade.json", new TypeReference<List<BybitTrade>>() {
                }));

        List<Trade> trades = restPublicDataService.getTrades(currencyPair, startTime, endTime);

        assertTrue(trades != null && !trades.isEmpty());
        assertTrue(trades.stream().allMatch(trade -> trade.getCurrencyPair().equals(currencyPair)));
        assertTrue(trades.stream().allMatch(trade -> trade.getTimestamp() <= endTime && trade.getTimestamp() >= startTime));
    }
}
