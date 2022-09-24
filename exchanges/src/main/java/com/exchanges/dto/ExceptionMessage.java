package com.exchanges.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExceptionMessage {
    String status;
    String message;

    public ExceptionMessage(String message) {
        this.status = "exception";
        this.message = message;
    }
}
