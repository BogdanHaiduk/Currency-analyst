package com.exchanges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageEvent {
    String exchange;
    CurrencyPair currencyPair;
    TypeEvent typeEvent;
    Topic topic;

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = new CurrencyPair(currencyPair);
    }
}
