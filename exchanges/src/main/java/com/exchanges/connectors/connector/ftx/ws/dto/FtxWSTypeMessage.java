package com.exchanges.connectors.connector.ftx.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum FtxWSTypeMessage {
    @JsonProperty("partial")
    PARTIAL("partial"),

    @JsonProperty("update")
    UPDATE("update"),

    @JsonProperty("unsubscribed")
    UNSUBSCRIBED("unsubscribed"),

    @JsonProperty("subscribed")
    SUBSCRIBED("subscribed");

    final String nameType;
}
