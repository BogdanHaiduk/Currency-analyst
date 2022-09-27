package com.analyst.dto;

import com.analyst.db.entity.StatusTask;
import com.analyst.db.entity.TopicTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusTaskDto {
    @Schema(example = "q2tvs63vbs62872hgsy33weh3d")
    String taskID;

    @Schema(example = "VIRTUAL_TRADE")
    TopicTask topicTask;

    @Schema(example = "CREATED")
    StatusTask statusTask;
}
