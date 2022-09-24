package com.exchanges.connectors.connector.ftx.rest.connection;

import com.exchanges.connectors.connector.ftx.rest.dto.FtxMarket;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxOrderBookRest;
import com.exchanges.connectors.connector.ftx.rest.dto.FtxTradeRest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxRestConnectionExchangeImpl implements FtxRestConnectionExchange {
    final static String HOST_NAME = "https://ftx.com";
    final RestTemplate restTemplate;

    @Autowired
    public FtxRestConnectionExchangeImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getURLPath(String path) {
        return String.format("%s%s", HOST_NAME, path);
    }

    @Override
    public FtxOrderBookRest getOrderBook(String currencyPair) {
        Map<String, Object> urlVariables = new HashMap<String, Object>() {{
            put("market_name", currencyPair);
            put("depth", DEPTH_ORDER_BOOK);
        }};

        ResponseEntity<FtxOrderBookRest> response = restTemplate.exchange(
                getURLPath("/api/markets/{market_name}/orderbook?depth={depth}"),
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                FtxOrderBookRest.class,
                urlVariables);

        return response.getBody();
    }

    @Override
    public FtxTradeRest getTrades(String currencyPair, long... timings) {
        String url = getURLPath("/api/markets/{market_name}/trades?start_time={start_time}&end_time={end_time}");

        Map<String, Object> urlVariables = new HashMap<String, Object>() {{
            put("market_name", currencyPair);
            put("start_time", timings[0]);
            put("end_time", timings[1]);
        }};

        ResponseEntity<FtxTradeRest> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                FtxTradeRest.class,
                urlVariables);

        return response.getBody();
    }

    @Override
    public FtxMarket getMarkets() {
        String url = getURLPath("/api/markets");

        ResponseEntity<FtxMarket> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                FtxMarket.class
        );

        return response.getBody();
    }
}
