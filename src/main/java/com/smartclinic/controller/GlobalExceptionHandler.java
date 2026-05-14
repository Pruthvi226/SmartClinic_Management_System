package com.smartclinic.controller;

import com.smartclinic.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Global exception handler for both REST APIs and web controllers
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        ex.printStackTrace();
        return "error/generic";
    }

    /**
     * Handle IllegalArgumentException (400 Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle NullPointerException (500 Internal Server Error)
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(
            NullPointerException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "A null value was encountered during processing",
                request.getRequestURI());
        error.addDetail("exception", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle validation exceptions (400 Bad Request)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "One or more validation constraints were violated",
                request.getRequestURI());
        ex.getConstraintViolations().forEach(cv -> error.addDetail(cv.getPropertyPath().toString(), cv.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle DataIntegrityViolationException (409 Conflict)
     */
    @ExceptionHandler({
            org.springframework.dao.DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Data integrity violation. The record may already exist or violate constraints.",
                request.getRequestURI());
        error.addDetail("cause", ex.getCause() != null ? ex.getCause().getMessage() : "Unknown");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handle EntityNotFoundException (404 Not Found)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            RuntimeException ex, HttpServletRequest request) {
        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    ex.getMessage(),
                    request.getRequestURI());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // For other runtime exceptions, return 500
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Catch-all exception handler (500 Internal Server Error)
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
            Throwable ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI());
        error.addDetail("exceptionType", ex.getClass().getSimpleName());
        error.addDetail("message", ex.getMessage());

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
