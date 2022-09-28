package com.exchanges.dto;

import com.exchanges.exception.BadClientRequestException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(value = {"baseCurrency", "quoteCurrency"}, ignoreUnknown = true)
public class CurrencyPair {
    @Schema(example = "BTC-USDT", required = true)
    String name;
    String baseCurrency;
    String quoteCurrency;

    public CurrencyPair(String name) {
        this.name = name;
        createBaseQuoteCurrency();
    }

    public CurrencyPair(String baseCurrency, String quoteCurrency) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.name = String.format("%s-%s", baseCurrency, quoteCurrency);
    }

    public void setName(String name) {
        this.name = name;
        createBaseQuoteCurrency();
    }

    private void createBaseQuoteCurrency() {
        if (name.contains("-")) {
            this.baseCurrency = name.substring(0, name.indexOf("-"));
            this.quoteCurrency = name.substring(name.indexOf("-") + 1);
        } else
            throw new BadClientRequestException("Wrong currency pair format. Correct example: BTC-USDT");
    }
}
