package com.exchanges.connectors.connector.ftx.rest.dto;

import com.exchanges.connectors.connector.ftx.rest.dto.core.FtxResponseRest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxOrderBookRest extends FtxResponseRest<FtxOrderBookRest> {
    List<BigDecimal[]> bids;
    List<BigDecimal[]> asks;
}
