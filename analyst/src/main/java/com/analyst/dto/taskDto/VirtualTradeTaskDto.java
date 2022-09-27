package com.analyst.dto.taskDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VirtualTradeTaskDto extends TaskDto {
    //currency, amount
    @Schema(example = "{\n\"BTC\": 0.44,\n\"DOGE\": 33.44,\n\"USDT\": 578\n}", required = true,
            description = "Сurrencies and their balances at the beginning of virtual trading")
    Map<String, BigDecimal> amountCurrency;

    public VirtualTradeTaskDto() {
        super();
    }

    public VirtualTradeTaskDto(Map<String, BigDecimal> amountCurrency) {
        super();
        this.amountCurrency = amountCurrency;
    }
}
