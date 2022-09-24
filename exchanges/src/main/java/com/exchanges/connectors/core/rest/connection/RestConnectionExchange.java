package com.exchanges.connectors.core.rest.connection;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

public interface RestConnectionExchange {
    int DEPTH_ORDER_BOOK = 20;

    default HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
