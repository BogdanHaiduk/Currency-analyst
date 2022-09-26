package com.exchanges.connectors.connector.bybit.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitTrade {
    BigDecimal price;
    Long time;
    BigDecimal qty;
    int isBuyerMaker;
}
