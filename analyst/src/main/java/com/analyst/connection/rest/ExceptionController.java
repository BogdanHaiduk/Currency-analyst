package com.analyst.connection.rest;

import com.analyst.dto.ExceptionMessageDto;
import com.analyst.exception.TaskClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessageDto> checkTaskClientException(TaskClientException taskClientException) {
        return new ResponseEntity<>(new ExceptionMessageDto(taskClientException.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
