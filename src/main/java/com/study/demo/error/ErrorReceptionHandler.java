package com.study.demo.error;

import com.study.demo.dto.BaseErrorResponseDTO;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.AmountSmallerThanMinimumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.SenderNotValidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorReceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SenderNotValidException.class)
    protected ResponseEntity<?> handleSenderNotValidConflict(SenderNotValidException ex, WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.UNAUTHORIZED, "Sender bank not valid", ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedTransactionException.class)
    protected ResponseEntity<?> handleDuplicatedTransactionConflict(DuplicatedTransactionException ex,
            WebRequest request) {
        return buildResponseEntity(
                new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST, "Duplicated transaction", ex.getMessage()));
    }

    @ExceptionHandler({ AmountGreaterThanMaximumException.class, AmountSmallerThanMinimumException.class })
    protected ResponseEntity<?> handleAmountBoundariesConflict(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(new BaseErrorResponseDTO(HttpStatus.BAD_REQUEST,
                "Transaction amount outside of the limits", ex.getMessage()));
    }

    private ResponseEntity<?> buildResponseEntity(BaseErrorResponseDTO error) {
        return ResponseEntity.status(error.getStatus()).contentType(MediaType.APPLICATION_JSON).body(error);
    }
}