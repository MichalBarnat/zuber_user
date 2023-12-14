package com.bbc.zuber.exception;

import com.bbc.zuber.exception.dto.ValidationErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorMessage> createErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .code(status.value())
                .status(status.getReasonPhrase())
                .message(ex.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> rideRequestNotFoundExceptionHandler(RideRequestNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> userNotFoundExceptionHandler(UserNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<ValidationErrorDto>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                ex.getFieldErrors().stream()
                        .map(
                                fieldError -> new ValidationErrorDto(fieldError.getDefaultMessage(), fieldError.getField())
                        ).toList());
    }
}
