package com.study.demo.error;

import javax.ws.rs.Produces;

import com.study.demo.dto.BaseErrorResponseDTO;
import com.study.demo.exception.SenderNotValidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorReceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SenderNotValidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<?> handleConflict(SenderNotValidException ex, WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.UNAUTHORIZED, "Sender bank not valid", ex.getMessage()));
    }

    private ResponseEntity<?> buildResponseEntity(BaseErrorResponseDTO error) {
        return ResponseEntity.status(error.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }
}