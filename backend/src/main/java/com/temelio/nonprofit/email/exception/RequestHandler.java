package com.temelio.nonprofit.email.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class RequestHandler {
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorResponse> handlerBadRequestException(WebRequest req, BadRequestException e) {
        log.info(e.getMessage());
        return sendBadRequest(e.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handlerException(WebRequest req, Exception e) {
        return sendInternalServerError(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> sendBadRequest(String message) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> sendInternalServerError(String message) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),  message);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
