package com.exchanges.connectors.connector.ftx.rest.connection;

import com.exchanges.connectors.connector.ftx.rest.dto.FtxMarket;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import com.exchanges.connectors.core.rest.connection.RestConnectionExchange;

public interface FtxRestConnectionExchange extends RestConnectionExchange {
    FtxOrderBookRest getOrderBook(String currencyPair);

    FtxTradeRest getTrades(String currencyPair, long... timings);

    FtxMarket getMarkets();
}
