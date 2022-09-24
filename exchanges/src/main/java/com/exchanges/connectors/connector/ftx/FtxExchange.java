package com.exchanges.connectors.connector.ftx;

import com.exchanges.connectors.core.Exchange;
import com.exchanges.connectors.core.rest.service.RestPublicDataService;
import com.exchanges.connectors.core.ws.service.WsPublicDataService;
import com.exchanges.connectors.core.ws.connection.WsConnectionExchange;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxExchange extends Exchange {
    static final String EXCHANGE_NAME = "ftx";
    final WsPublicDataService wsPublicDataService;
    final WsConnectionExchange wsConnectionExchange;
    final RestPublicDataService restPublicDataService;

    @Autowired
    public FtxExchange(
            @Qualifier("ftxWsPublicDataService") WsPublicDataService wsPublicDataService,
            @Qualifier("ftxWsConnectionExchange") WsConnectionExchange wsConnectionExchange,
            @Qualifier("ftxWSHandlerExchange") WsHandlerExchange wsHandlerExchange,
            @Qualifier("ftxRestPublicDataService") RestPublicDataService restPublicDataService) {
        super(wsHandlerExchange);
        this.wsPublicDataService = wsPublicDataService;
        this.wsConnectionExchange = wsConnectionExchange;
        this.restPublicDataService = restPublicDataService;
    }

    @Override
    public String getExchangeName() {
        return EXCHANGE_NAME;
    }

    @Override
    public WsConnectionExchange getWsConnectionExchange() {
        return wsConnectionExchange;
    }

    @Override
    public WsPublicDataService getWSPublicDataService() {
        return wsPublicDataService;
    }

    @Override
    public RestPublicDataService getRestPublicDataService() {
        return restPublicDataService;
    }
}
