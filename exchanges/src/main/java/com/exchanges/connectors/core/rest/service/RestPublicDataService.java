package com.exchanges.connectors.core.rest.service;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;

import java.util.List;

public interface RestPublicDataService {
    OrderBook getOrderBook(CurrencyPair currencyPair);

    List<Trade> getTrades(CurrencyPair currencyPair, long... timings);

    List<CurrencyPair> getCurrencyPairsBySpotMarket();
}
