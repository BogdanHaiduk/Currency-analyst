package com.exchanges.connectors.connector.bybit.rest.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitOrderBook {
    Long time;
    List<BigDecimal[]> asks;
    List<BigDecimal[]> bids;
}
