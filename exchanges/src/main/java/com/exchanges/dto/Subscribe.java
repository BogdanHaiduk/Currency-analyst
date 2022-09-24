package com.exchanges.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Subscribe {
    String currencyPair;
    Topic topic;
}
