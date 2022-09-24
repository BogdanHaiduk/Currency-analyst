package com.exchanges.connection.rest;

import com.exchanges.connectors.core.Exchange;
import com.exchanges.connectors.core.FactoryExchange;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.OrderBook;
import com.exchanges.dto.Trade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Public endpoints (not need authentication) MS Exchanges.",
        description = "API for working with public endpoints of currency exchange connectors.")
public class ControllerRest {
    FactoryExchange factoryExchange;

    @Autowired
    public ControllerRest(FactoryExchange factoryExchange) {
        this.factoryExchange = factoryExchange;
    }

    @GetMapping("/orderBook")
    @Operation(description = "Endpoint for get order book by exchange name and currency pair.")
    public OrderBook getOrderBook(@RequestParam("exchangeName") String exchangeName,
                                  @RequestParam("currencyPair") String currencyPairName) {
        Exchange exchange = factoryExchange.getExchange(exchangeName);

        return exchange.getRestPublicDataService().getOrderBook(new CurrencyPair(currencyPairName));
    }

    @GetMapping("/trades")
    @Operation(description = "Endpoint for get trades by exchange name and currency pair." +
            "Params startTime and endTime timestamp is in milliseconds.")
    public List<Trade> getOrderBook(@RequestParam("exchangeName") String exchangeName,
                                    @RequestParam("currencyPair") String currencyPairName,
                                    @RequestParam(value = "startTime", required = false, defaultValue = "0") Long startTime,
                                    @RequestParam(value = "endTime", required = false, defaultValue = "0") Long endTime) {
        Exchange exchange = factoryExchange.getExchange(exchangeName);
        CurrencyPair currencyPair = new CurrencyPair(currencyPairName);

        return exchange.getRestPublicDataService().getTrades(currencyPair, startTime, endTime);
    }

    @GetMapping("/currencyPair/spot")
    @Operation(description = "Endpoint for get all currency pairs spot market in market by exchange name.")
    public List<CurrencyPair> getAllCurrencyPairSpotByExchange(@RequestParam("exchangeName") String exchangeName) {
        Exchange exchange = factoryExchange.getExchange(exchangeName);

        return exchange.getRestPublicDataService().getCurrencyPairsBySpotMarket();
    }
}
