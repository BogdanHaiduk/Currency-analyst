package com.exchanges.connection.rest;

import com.exchanges.exception.FichaNotImplementException;
import com.exchanges.dto.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> checkFichaNotImplementException(FichaNotImplementException fichaNotImplementException) {
        return new ResponseEntity<>(
                new ExceptionMessage(fichaNotImplementException.getMessage()),
                HttpStatus.NOT_IMPLEMENTED
        );
    }
}
