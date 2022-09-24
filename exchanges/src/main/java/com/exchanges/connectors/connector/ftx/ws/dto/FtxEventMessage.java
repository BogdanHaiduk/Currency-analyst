package com.exchanges.connectors.connector.ftx.ws.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxEventMessage {
    String op;
    String channel;
    String market;
}
