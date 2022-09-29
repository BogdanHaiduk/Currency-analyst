package com.exchanges.connectors.core.rest.service;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import com.exchanges.exception.FeatureNotImplementException;

import java.util.List;

public interface RestPublicDataService {
    default OrderBook getOrderBook(CurrencyPair currencyPair) {
        throw new FeatureNotImplementException("Feature get order book not implemented.");
    }

    default List<Trade> getTrades(CurrencyPair currencyPair, long... timings) {
        throw new FeatureNotImplementException("Feature get trades not implemented.");
    }

    default List<CurrencyPair> getCurrencyPairsBySpotMarket() {
        throw new FeatureNotImplementException("Feature get currency pairs by spot market not implemented.");
    }
}
