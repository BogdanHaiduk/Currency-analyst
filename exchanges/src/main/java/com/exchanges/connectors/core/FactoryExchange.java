package com.exchanges.connectors.core;

import com.exchanges.exception.FeatureNotImplementException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FactoryExchange {
    final Map<String, Exchange> exchanges = new HashMap<>();

    @Autowired
    private FactoryExchange(List<Exchange> baseExchanges) {
        baseExchanges.forEach(
                exchange -> exchanges.put(exchange.getExchangeName(), exchange)
        );
    }

    public Exchange getExchange(String exchangeNameCreate) {
        return exchanges.entrySet().stream()
                .filter(exchangeEntry -> exchangeEntry.getKey().equals(exchangeNameCreate))
                .findFirst()
                .orElseThrow(() -> new FeatureNotImplementException(String.format("%s API not implement.", exchangeNameCreate)))
                .getValue();
    }
}