package com.exchanges.connectors.connector.bybit.rest.connection;

import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitOrderBook;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitTrade;
import com.exchanges.connectors.core.rest.connection.RestConnectionExchange;

import java.util.List;

public interface BybitRestConnectionExchange extends RestConnectionExchange {
    BybitOrderBook getOrderBook(String currencyPair);

    List<BybitTrade> getTrades(String currencyPair);

    List<BybitMarket> getMarkets();
}
