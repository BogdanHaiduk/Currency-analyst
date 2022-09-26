package com.exchanges.connectors.connector.bybit.rest.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitRestResponse<T> {
    int retCode;
    String retMsg;
    T result;
}
