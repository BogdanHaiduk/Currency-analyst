package com.exchanges.connectors.connector.bybit.rest.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitRestList<T> extends BybitRestResponse<BybitRestList<T>> {
    List<T> list;
}