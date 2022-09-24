package com.exchanges.connectors.connector.ftx.rest.dto;

import com.exchanges.connectors.connector.ftx.rest.dto.core.FtxResponseRest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxMarket extends FtxResponseRest<List<FtxMarket>> {
    String name;
    String type;
}
