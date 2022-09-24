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
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FtxTradeRest extends FtxResponseRest<List<FtxTradeRest>> {
    long id;
    boolean liquidation;
    BigDecimal price;
    String side;
    BigDecimal size;
    String time;
}
