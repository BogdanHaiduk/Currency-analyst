package com.exchanges.connectors.connector.bybit;

import com.exchanges.connectors.core.Exchange;
import com.exchanges.connectors.core.rest.service.RestPublicDataService;
import com.exchanges.connectors.core.ws.connection.WsConnectionExchange;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import com.exchanges.connectors.core.ws.service.WsPublicDataService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BybitExchange extends Exchange {
    final WsPublicDataService wsPublicDataService;
    final WsConnectionExchange wsConnectionExchange;
    final RestPublicDataService restPublicDataService;

    public BybitExchange(@Qualifier("bybitWsPublicDataService") WsPublicDataService wsPublicDataService,
                         @Qualifier("bybitWsConnectionExchange") WsConnectionExchange wsConnectionExchange,
                         @Qualifier("bybitRestPublicDataService") RestPublicDataService restPublicDataService,
                         @Qualifier("bybitWsHandlerExchange") WsHandlerExchange wsHandlerExchange) {
        super(wsHandlerExchange);
        this.wsPublicDataService = wsPublicDataService;
        this.wsConnectionExchange = wsConnectionExchange;
        this.restPublicDataService = restPublicDataService;
    }

    @Override
    public String getExchangeName() {
        return "bybit";
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
