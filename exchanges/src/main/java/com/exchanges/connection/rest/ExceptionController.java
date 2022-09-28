package com.exchanges.connection.rest;

import com.exchanges.exception.BadClientRequestException;
import com.exchanges.exception.FeatureNotImplementException;
import com.exchanges.dto.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> checkFichaNotImplementException(FeatureNotImplementException featureNotImplementException) {
        return new ResponseEntity<>(
                new ExceptionMessage(featureNotImplementException.getMessage()),
                HttpStatus.NOT_IMPLEMENTED
        );
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> checkBadClientRequestException(BadClientRequestException badClientRequestException) {
        return new ResponseEntity<>(
                new ExceptionMessage(badClientRequestException.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
