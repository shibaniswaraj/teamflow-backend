package com.teamflow.teamflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ Validation errors (@Valid DTO failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", errorMessage
                        )
                );
    }

    // 2️⃣ Business logic errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", ex.getMessage()
                        )
                );
    }


    // 1️⃣ Header / type conversion errors (like X-ROLE)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", "Invalid value for " + ex.getName()
                        )
                );
    }

    // Missing header errors (like X-ROLE not usable)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(
            MissingRequestHeaderException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", "Missing or invalid header: " + ex.getHeaderName()
                        )
                );
    }


    // 3️⃣ Fallback (true server errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of(
                                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "error", "Something went wrong"
                        )
                );
    }
}
