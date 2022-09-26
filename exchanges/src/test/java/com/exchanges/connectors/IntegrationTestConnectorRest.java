package com.exchanges.connectors;

import com.exchanges.connectors.core.Exchange;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegrationTestConnectorRest {
    static String CURRENCY_PAIR = "BTC-USDT";
    static String QUERY_PARAMS_EXCHANGE_NAME = "exchangeName";
    static String QUERY_PARAMS_CURRENCY_PAIR = "currencyPair";
    static String QUERY_PARAMS_START_TIME = "startTime";
    static String QUERY_PARAMS_END_TIME = "endTime";

    final TestRestTemplate testRestTemplate = new TestRestTemplate();
    final List<String> exchanges;

    @Value("${server.test.host}")
    String serverHost;

    @Autowired
    public IntegrationTestConnectorRest(List<Exchange> exchanges) {
        this.exchanges = new ArrayList<>();
        exchanges.forEach(exchange -> this.exchanges.add(exchange.getExchangeName()));
    }

    private String createUrl(String urlPath) {
        return String.format("%s/api/%s", serverHost, urlPath);
    }

    private void doLog(String exchange, String testCase) {
        log.info("Connector {} successfully passed the test case - {}", exchange, testCase);
    }

    @Test
    public void integrationTestCase_getOrderBookByEndpoint_orderBook() {
        exchanges.forEach(exchange -> {
            Map<String, Object> urlVariables = new HashMap<>();
            urlVariables.put(QUERY_PARAMS_EXCHANGE_NAME, exchange);
            urlVariables.put(QUERY_PARAMS_CURRENCY_PAIR, CURRENCY_PAIR);

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
            assertEquals(exchange, orderBook.getExchangeName());
            assertEquals(new CurrencyPair(CURRENCY_PAIR), orderBook.getCurrencyPair());

            doLog(exchange, "integrationTestCase_getOrderBookByEndpoint_orderBook");
        });
    }

    @Test
    public void integrationTestCase_getListCurrencyPairByEndpoint_listCurrencyPairs() {
        exchanges.forEach(exchange -> {
            ResponseEntity<List<CurrencyPair>> response = testRestTemplate.exchange(
                    createUrl("/currencyPair/spot?exchangeName={exchangeName}"),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyPair>>() {
                    },
                    Collections.singletonMap(QUERY_PARAMS_EXCHANGE_NAME, exchange));

            List<CurrencyPair> currencyPairs = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(currencyPairs);
            assertFalse(currencyPairs.isEmpty());

            doLog(exchange, "integrationTestCase_getListCurrencyPairByEndpoint_listCurrencyPairs");
        });
    }

    @Test
    public void integrationTestCase_getTradesByEndpointWithoutTimingParams_listTrades() {
        exchanges.forEach(exchange -> {
            Map<String, Object> urlVariables = new HashMap<>();
            urlVariables.put(QUERY_PARAMS_EXCHANGE_NAME, exchange);
            urlVariables.put(QUERY_PARAMS_CURRENCY_PAIR, CURRENCY_PAIR);

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

            doLog(exchange, "integrationTestCase_getTradesByEndpointWithoutTimingParams_listTrades");
        });
    }

    @Test
    public void integrationTestCase_getTradesByEndpointWithTimingParams_listTrades() {
        exchanges.forEach(exchange -> {
            long timeNow = System.currentTimeMillis();
            //1000000 milliseconds - 1000 seconds - 16 min 40 sec
            long startTime = timeNow - 1000000;
            long endTime = timeNow;

            Map<String, Object> urlVariables = new HashMap<>();
            urlVariables.put(QUERY_PARAMS_EXCHANGE_NAME, exchange);
            urlVariables.put(QUERY_PARAMS_CURRENCY_PAIR, CURRENCY_PAIR);
            urlVariables.put(QUERY_PARAMS_START_TIME, startTime);
            urlVariables.put(QUERY_PARAMS_END_TIME, endTime);

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

            doLog(exchange, "integrationTestCase_getTradesByEndpointWithTimingParams_listTrades");
        });
    }
}
