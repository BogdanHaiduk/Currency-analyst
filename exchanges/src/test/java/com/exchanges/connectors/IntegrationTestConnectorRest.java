package com.exchanges.connectors;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegrationTestConnectorRest {
    final TestRestTemplate testRestTemplate = new TestRestTemplate();
    @Value("${server.test.host}")
    String serverHost;

    private String createUrl(String urlPath) {
        return String.format("%s/api/%s", serverHost, urlPath);
    }

    @Test
    public void integrationTestCase_getOrderBookByEndpoint_orderBook() {
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("exchangeName", "ftx");
        urlVariables.put("currencyPair", "BTC-USDT");

        ResponseEntity<OrderBook> response = testRestTemplate.exchange(
                createUrl("/orderBook?exchangeName={exchangeName}&currencyPair={currencyPair}"),
                HttpMethod.GET,
                null,
                OrderBook.class,
                urlVariables);

        OrderBook orderBook = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(orderBook);
        assertFalse(orderBook.getBids().isEmpty());
        assertFalse(orderBook.getAsks().isEmpty());
        assertEquals("ftx", orderBook.getExchangeName());
        assertEquals(new CurrencyPair("BTC-USDT"), orderBook.getCurrencyPair());
    }

    @Test
    public void integrationTestCase_getListCurrencyPairByEndpoint_listCurrencyPairs() {
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("exchangeName", "ftx");

        ResponseEntity<List<CurrencyPair>> response = testRestTemplate.exchange(
                createUrl("/currencyPair/spot?exchangeName={exchangeName}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CurrencyPair>>() {
                },
                urlVariables);

        List<CurrencyPair> currencyPairs = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(currencyPairs);
        assertFalse(currencyPairs.isEmpty());
    }

    @Test
    public void integrationTestCase_getTradesByEndpointWithoutTimingParams_listTrades() {
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("exchangeName", "ftx");
        urlVariables.put("currencyPair", "BTC-USDT");

        ResponseEntity<List<Trade>> response = testRestTemplate.exchange(
                createUrl("/trades?exchangeName={exchangeName}&currencyPair={currencyPair}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Trade>>() {
                },
                urlVariables);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && !response.getBody().isEmpty());
        assertTrue(response.getBody()
                .stream()
                .allMatch(trade -> trade.getCurrencyPair().equals(new CurrencyPair("BTC-USDT")))
        );
    }

    @Test
    public void integrationTestCase_getTradesByEndpointWithTimingParams_listTrades() {
        long timeNow = System.currentTimeMillis();
        //1000000 milliseconds - 1000 seconds - 16 min 40 sec
        long startTime = timeNow - 1000000;
        long endTime = timeNow;

        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("exchangeName", "ftx");
        urlVariables.put("currencyPair", "BTC-USDT");
        urlVariables.put("startTime", startTime);
        urlVariables.put("endTime", endTime);

        String urlPath = "/trades?exchangeName={exchangeName}&currencyPair={currencyPair}&startTime={startTime}&endTime={endTime}";

        ResponseEntity<List<Trade>> response = testRestTemplate.exchange(
                createUrl(urlPath),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Trade>>() {
                },
                urlVariables);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && !response.getBody().isEmpty());
        assertTrue(response.getBody()
                .stream()
                .allMatch(trade -> trade.getTimestamp() >= startTime && endTime >= trade.getTimestamp()));
    }
}
