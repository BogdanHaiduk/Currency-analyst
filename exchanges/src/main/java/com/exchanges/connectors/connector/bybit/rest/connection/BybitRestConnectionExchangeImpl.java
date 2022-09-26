package com.exchanges.connectors.connector.bybit.rest.connection;

import com.exchanges.connectors.connector.bybit.rest.dto.BybitMarket;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitOrderBook;
import com.exchanges.connectors.connector.bybit.rest.dto.BybitTrade;
import com.exchanges.connectors.connector.bybit.rest.dto.core.BybitRestList;
import com.exchanges.connectors.connector.bybit.rest.dto.core.BybitRestResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitRestConnectionExchangeImpl implements BybitRestConnectionExchange {
    final static String HOST_NAME = "https://api.bybit.com";
    final RestTemplate restTemplate;

    @Autowired
    public BybitRestConnectionExchangeImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getURLPath(String path) {
        return String.format("%s%s", HOST_NAME, path);
    }

    @Override
    public BybitOrderBook getOrderBook(String currencyPair) {
        ResponseEntity<BybitRestResponse<BybitOrderBook>> response = restTemplate.exchange(
                getURLPath("/spot/v3/public/quote/depth/merged?symbol={symbol}"),
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                new ParameterizedTypeReference<BybitRestResponse<BybitOrderBook>>() {
                },
                Collections.singletonMap("symbol", currencyPair)
        );

        if (response.getBody() != null)
            return response.getBody().getResult();

        return new BybitOrderBook();
    }

    @Override
    public List<BybitTrade> getTrades(String currencyPair) {
        ResponseEntity<BybitRestResponse<BybitRestList<BybitTrade>>> response = restTemplate.exchange(
                getURLPath("/spot/v3/public/quote/trades?symbol={symbol}"),
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                new ParameterizedTypeReference<BybitRestResponse<BybitRestList<BybitTrade>>>() {
                },
                Collections.singletonMap("symbol", currencyPair)
        );

        if (response.getBody() != null)
            return response.getBody().getResult().getList();

        return Collections.emptyList();
    }

    @Override
    public List<BybitMarket> getMarkets() {
        ResponseEntity<BybitRestResponse<BybitRestList<BybitMarket>>> response = restTemplate.exchange(
                getURLPath("/spot/v3/public/symbols"),
                HttpMethod.GET,
                new HttpEntity<>(getHeader()),
                new ParameterizedTypeReference<BybitRestResponse<BybitRestList<BybitMarket>>>() {
                });

        if (response.getBody() != null)
            return response.getBody().getResult().getList();

        return Collections.emptyList();
    }
}
