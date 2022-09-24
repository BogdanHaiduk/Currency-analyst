package com.exchanges.connectors.connector.ftx;

import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import com.exchanges.connectors.connector.ftx.ws.dto.orderBook.FtxWSDataOrderBook;
import com.exchanges.connectors.connector.ftx.ws.dto.orderBook.FtxWsOrderBook;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Side;
import com.exchanges.dto.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class FtxAdapter {
    public static String toCurrencyPairFtx(CurrencyPair currencyPair) {
        return String.format("%s/%s", currencyPair.getBaseCurrency(), currencyPair.getQuoteCurrency());
    }

    public static CurrencyPair toCurrencyPair(String currencyPairFtx) {
        return new CurrencyPair(
                currencyPairFtx.substring(0, currencyPairFtx.indexOf("/")),
                currencyPairFtx.substring(currencyPairFtx.indexOf("/") + 1)
        );
    }

    public static OrderBook toOrderBook(FtxWsOrderBook ftxWsOrderBook) {
        if (ftxWsOrderBook != null && ftxWsOrderBook.getFtxWSDataOrderBook() != null) {
            FtxWSDataOrderBook ftxWSDataOrderBook = ftxWsOrderBook.getFtxWSDataOrderBook();

            return OrderBook.builder("ftx", ftxWSDataOrderBook.getTimestamp())
                    .currencyPair(toCurrencyPair(ftxWsOrderBook.getMarket()))
                    .asks(createAskAndBid(ftxWSDataOrderBook.getAsks()))
                    .bids(createAskAndBid(ftxWSDataOrderBook.getBids()))
                    .build();
        }

        return OrderBook.builder("ftx", new Date().getTime()).build();
    }

    public static OrderBook toOrderBook(FtxOrderBookRest ftxOrderBookRest, CurrencyPair currencyPair) {
        if (ftxOrderBookRest != null)
            return OrderBook.builder("ftx", new Date().getTime())
                    .currencyPair(currencyPair)
                    .asks(createAskAndBid(ftxOrderBookRest.getResult().getAsks()))
                    .bids(createAskAndBid(ftxOrderBookRest.getResult().getBids()))
                    .build();

        return OrderBook.builder("ftx", new Date().getTime()).build();
    }

    private static Map<BigDecimal, BigDecimal> createAskAndBid(List<BigDecimal[]> priceAndAmount) {
        if (priceAndAmount != null) {
            Map<BigDecimal, BigDecimal> askOrBid = new HashMap<>();
            priceAndAmount.forEach(orderBid -> askOrBid.put(orderBid[0], orderBid[1]));

            return askOrBid;
        }

        return Collections.emptyMap();
    }

    public static List<Trade> toListTrade(FtxTradeRest ftxTrades, CurrencyPair currencyPair) {
        if (ftxTrades != null)
            return ftxTrades.getResult().stream()
                    .map(ftxTrade ->
                            Trade.builder()
                                    .id(String.valueOf(ftxTrade.getId()))
                                    .currencyPair(currencyPair)
                                    .exchangeName("ftx")
                                    .price(ftxTrade.getPrice())
                                    .side(ftxTrade.getSide().equals("buy") ? Side.BID : Side.ASK)
                                    .size(ftxTrade.getSize())
                                    .timestamp(toTimestamp(ftxTrade.getTime()).getTime())
                                    .build())
                    .collect(Collectors.toList());

        return Collections.emptyList();
    }

    public static Date toTimestamp(String time) {
        if (time != null) {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                    .parseLenient()
                    .appendOffset("+HH:MM", "Z")
                    .toFormatter();
            LocalDateTime ldt = LocalDateTime.parse(time, dateTimeFormatter);

            return Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());
        }

        return new Date();
    }
}
