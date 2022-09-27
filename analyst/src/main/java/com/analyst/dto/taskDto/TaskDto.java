package com.analyst.dto.taskDto;

import com.analyst.db.entity.TopicTask;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {
    @Schema(example = "q2tvs63vbs62872hgsy33weh3d", required = true)
    String clientID;

    @Schema(example = "30", required = true)
    int minutesInterval;

    @Schema(example = "VIRTUAL_TRADE", required = true)
    TopicTask topicTask;

    @Schema(example = "[\"ftx\",\"bybit\"]", required = true)
    List<String> exchanges;

    @Schema(example = "[\"BTC-USDT\", \"DOGE-USDT\"]", required = true)
    List<String> currencyPairs;
}
