package com.exchanges.connectors.connector.ftx.ws.dto.orderBook;

import com.exchanges.connectors.connector.ftx.ws.dto.FtxWSTypeMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxWsOrderBook {
    String channel;
    String market;
    FtxWSTypeMessage type;
    @JsonProperty(value = "data")
    FtxWSDataOrderBook ftxWSDataOrderBook;
}
