package com.exchanges.connectors.connector.ftx.ws.dto.orderBook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxWSDataOrderBook extends FtxWsOrderBook {
    @JsonProperty(value = "time")
    long timestamp;
    long checksum;
    List<BigDecimal[]> bids;
    List<BigDecimal[]> asks;
    String action;

    public void setTimestamp(String timestampNano) {
        Double timestamp = Double.parseDouble(timestampNano) * 1000;
        this.timestamp = timestamp.longValue();
    }
}

