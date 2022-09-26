package com.exchanges.connectors.connector.bybit.rest.service;

import com.exchanges.connectors.connector.bybit.BybitAdapter;
import com.exchanges.connectors.connector.bybit.rest.connection.BybitRestConnectionExchange;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.core.rest.service.RestPublicDataService;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitRestPublicDataService implements RestPublicDataService {
    final BybitRestConnectionExchange bybitRestConnectionExchange;

    @Autowired
    public BybitRestPublicDataService(BybitRestConnectionExchange bybitRestConnectionExchange) {
        this.bybitRestConnectionExchange = bybitRestConnectionExchange;
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair) {
        String currencyPairBybitFormat = BybitAdapter.toCurrencyPair(currencyPair);

        return BybitAdapter.toOrderBook(bybitRestConnectionExchange.getOrderBook(currencyPairBybitFormat), currencyPair);
    }

    @Override
    public List<Trade> getTrades(CurrencyPair currencyPair, long... timings) {
        String currencyPairBybitFormat = BybitAdapter.toCurrencyPair(currencyPair);

        if (timings == null || timings.length == 0)
            return BybitAdapter.toListTrades(bybitRestConnectionExchange.getTrades(currencyPairBybitFormat), currencyPair);

        if (timings.length == 2 && timings[0] != 0 && timings[1] != 0) {
            long timeStart = Math.min(timings[0], timings[1]);
            long timeEnd = Math.max(timings[0], timings[1]);

            return BybitAdapter.toListTrades(bybitRestConnectionExchange.getTrades(currencyPairBybitFormat), currencyPair)
                    .stream()
                    .filter(trade -> trade.getTimestamp() >= timeStart && trade.getTimestamp() <= timeEnd)
                    .collect(Collectors.toList());
        }

        return BybitAdapter.toListTrades(bybitRestConnectionExchange.getTrades(currencyPairBybitFormat), currencyPair)
                .stream()
                .filter(trade -> trade.getTimestamp() >= timings[0])
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencyPair> getCurrencyPairsBySpotMarket() {
        List<BybitMarket> bybitRestList = bybitRestConnectionExchange.getMarkets();

        return bybitRestList
                .stream()
                .map(bybitMarket -> new CurrencyPair(bybitMarket.getBaseCoin(), bybitMarket.getQuoteCoin()))
                .collect(Collectors.toList());
    }
}
