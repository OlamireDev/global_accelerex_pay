package com.olamiredev.accelepay.exception.handler;

import com.olamiredev.accelepay.exception.APException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class APExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        log.error("Exception occurred: {}", ex.getMessage());
        if(ex instanceof APException apException){
            log.warn("APException occurred: {}", apException.getClassOfOrigin());//Log the class of origin, this allows us to trace the source of the exception
            apException.setClassOfOrigin(null);
            return new ResponseEntity<>(apException, HttpStatusCode.valueOf(apException.getErrorType().getResponseCode()));
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
