package com.exchanges.connectors.connector.bybit.ws.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitWSOrderBook extends BybitWSResponse<BybitWSOrderBook> {
    @JsonProperty("s")
    String market;

    @JsonProperty("t")
    Long timestamp;

    @JsonProperty("b")
    List<BigDecimal[]> bids;

    @JsonProperty("a")
    List<BigDecimal[]> asks;
}
