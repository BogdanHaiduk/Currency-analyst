package com.exchanges.connection.rabbit.service;

import com.exchanges.dto.MessageEvent;
import com.exchanges.dto.TypeEvent;
import com.exchanges.connectors.core.Exchange;
import com.exchanges.connectors.core.FactoryExchange;
import com.exchanges.exception.FeatureNotImplementException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandServiceImpl implements CommandService {
    final FactoryExchange factoryExchange;

    @Autowired
    public CommandServiceImpl(FactoryExchange factoryExchange) {
        this.factoryExchange = factoryExchange;
    }

    @Override
    public void handleMessage(MessageEvent messageEvent) {
        Exchange exchange = factoryExchange.getExchange(messageEvent.getExchange());

        if (!exchange.checkWorkSession())
            exchange.getWsConnectionExchange().connect();

        doEvent(messageEvent, exchange);
    }

    private void doEvent(MessageEvent messageEvent, Exchange exchange) {
        switch (messageEvent.getTopic()) {
            case ORDER_BOOK:
                if (messageEvent.getTypeEvent().equals(TypeEvent.SUBSCRIBE)) {
                    exchange.getWSPublicDataService().subscribeOrderBook(messageEvent.getCurrencyPair());
                }

                if (messageEvent.getTypeEvent().equals(TypeEvent.UNSUBSCRIBE))
                    exchange.getWSPublicDataService().unsubscribeOrderBook(messageEvent.getCurrencyPair());
                return;

            default:
                throw new FeatureNotImplementException(String.format(
                        "Event with topic %s not implement",
                        messageEvent.getTopic())
                );
        }
    }
}
