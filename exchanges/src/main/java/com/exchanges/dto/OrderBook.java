package com.exchanges.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBook {
    @Schema(example = "binance", required = true)
    final String exchangeName;

    @Schema(example = "{\n\"name\": \"BTC-USDT\"\n}", required = true)
    final CurrencyPair currencyPair;

    @Schema(example = "{\n\"18975.0\": 0.335,\n\"18982.0\": 2.0}", required = true)
    //price, amount
    final Map<BigDecimal, BigDecimal> asks;

    @Schema(example = "{\n\"18960.0\": 0.7614,\n\"18945.0\": 0.15}", required = true)
    //price, amount
    final Map<BigDecimal, BigDecimal> bids;

    @Schema(example = "1663783539530", required = true)
    Long timestamp;

    public OrderBook(@JsonProperty("exchangeName") String exchangeName,
                     @JsonProperty("currencyPair") CurrencyPair currencyPair,
                     @JsonProperty("asks") Map<BigDecimal, BigDecimal> asks,
                     @JsonProperty("bids") Map<BigDecimal, BigDecimal> bids,
                     @JsonProperty("timestamp") Long timestamp) {
        this.exchangeName = exchangeName;
        this.currencyPair = currencyPair;
        this.asks = asks;
        this.bids = bids;
        this.timestamp = timestamp;
    }

    private OrderBook(OrderBookBuilder orderBookBuilder) {
        this.exchangeName = orderBookBuilder.exchangeName;
        this.timestamp = orderBookBuilder.timestamp;
        this.currencyPair = orderBookBuilder.currencyPair;
        this.asks = orderBookBuilder.asks;
        this.bids = orderBookBuilder.bids;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static OrderBookBuilder builder(String exchangeName, Long timestamp) {
        return new OrderBookBuilder()
                .exchangeName(exchangeName)
                .timestamp(timestamp);
    }

    public static class OrderBookBuilder {
        String exchangeName;
        Long timestamp;
        CurrencyPair currencyPair;
        //price, amount
        Map<BigDecimal, BigDecimal> asks = new TreeMap<>();
        //price, amount
        Map<BigDecimal, BigDecimal> bids = new TreeMap<>(Comparator.reverseOrder());

        public OrderBookBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;

            return this;
        }

        public OrderBookBuilder exchangeName(String exchangeName) {
            this.exchangeName = exchangeName;

            return this;
        }

        public OrderBookBuilder currencyPair(CurrencyPair currencyPair) {
            this.currencyPair = currencyPair;

            return this;
        }

        public OrderBookBuilder asks(Map<BigDecimal, BigDecimal> asks) {
            this.asks.putAll(asks);

            return this;
        }

        public OrderBookBuilder bids(Map<BigDecimal, BigDecimal> bids) {
            this.bids.putAll(bids);

            return this;
        }

        public OrderBook build() {
            return new OrderBook(this);
        }
    }
}
