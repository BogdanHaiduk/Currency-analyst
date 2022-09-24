package com.exchanges.connectors.connector.ftx.rest.service;

import com.exchanges.connectors.connector.ftx.FtxAdapter;
import com.exchanges.connectors.connector.ftx.rest.connection.FtxRestConnectionExchange;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxMarket;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import com.exchanges.connectors.core.rest.service.RestPublicDataService;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxRestPublicDataService implements RestPublicDataService {
    final FtxRestConnectionExchange restConnectionExchange;

    public FtxRestPublicDataService(@Qualifier("ftxRestConnectionExchangeImpl") FtxRestConnectionExchange restConnectionExchange) {
        this.restConnectionExchange = restConnectionExchange;
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair) {
        FtxOrderBookRest ftxOrderBookRest = restConnectionExchange.getOrderBook(FtxAdapter.toCurrencyPairFtx(currencyPair));

        return FtxAdapter.toOrderBook(ftxOrderBookRest, currencyPair);
    }

    @Override
    public List<CurrencyPair> getCurrencyPairsBySpotMarket() {
        FtxMarket ftxMarkets = restConnectionExchange.getMarkets();

        return ftxMarkets.getResult()
                .stream()
                .filter(ftxMarket -> ftxMarket.getType().equals("spot"))
                .map(FtxMarket::getName)
                .map(FtxAdapter::toCurrencyPair)
                .collect(Collectors.toList());
    }

    @Override
    public List<Trade> getTrades(CurrencyPair currencyPair, long... timings) {
        String ftxCurrencyPair = FtxAdapter.toCurrencyPairFtx(currencyPair);
        FtxTradeRest ftxTradeRests = null;

        if (timings.length == 0) {
            ftxTradeRests = restConnectionExchange.getTrades(ftxCurrencyPair, 0, Instant.now().getEpochSecond());

            return FtxAdapter.toListTrade(ftxTradeRests, currencyPair);
        }

        if (timings.length == 2 && timings[0] != timings[1]) {
            long startTime = Math.min(timings[0], timings[1]) / 1000;
            long endTime = Math.max(timings[0], timings[1]) / 1000;
            ftxTradeRests = restConnectionExchange.getTrades(ftxCurrencyPair, startTime, endTime);

            return FtxAdapter.toListTrade(ftxTradeRests, currencyPair);
        }

        long startTime = timings[0];
        ftxTradeRests = restConnectionExchange.getTrades(ftxCurrencyPair, startTime, Instant.now().getEpochSecond());

        return FtxAdapter.toListTrade(ftxTradeRests, currencyPair);
    }
}
