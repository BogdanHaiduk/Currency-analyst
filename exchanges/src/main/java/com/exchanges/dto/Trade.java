package com.exchanges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trade {
    @Schema(example = "binance", required = true)
    String exchangeName;

    @Schema(example = "{\n\"name\": \"BTC-USDT\"\n}", required = true)
    CurrencyPair currencyPair;

    @Schema(example = "5005219443", required = true)
    String id;

    @Schema(example = "19431.0", required = true)
    BigDecimal price;

    @Schema(example = "ASK", required = true)
    Side side;

    @Schema(example = "0.0005", required = true)
    BigDecimal size;

    @Schema(example = "1663785569672", required = true)
    long timestamp;

    public Trade(@JsonProperty("exchangeName") String exchangeName,
                 @JsonProperty("currencyPair") CurrencyPair currencyPair,
                 @JsonProperty("id") String id,
                 @JsonProperty("price") BigDecimal price,
                 @JsonProperty("side") Side side,
                 @JsonProperty("size") BigDecimal size,
                 @JsonProperty("timestamp") long timestamp) {
        this.exchangeName = exchangeName;
        this.currencyPair = currencyPair;
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
        this.timestamp = timestamp;
    }
}
