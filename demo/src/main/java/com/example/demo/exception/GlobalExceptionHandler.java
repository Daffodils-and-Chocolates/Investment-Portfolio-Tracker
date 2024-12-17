package com.example.demo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ProblemDetail;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String description) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty("description", description);
        problemDetail.setProperty("timestamp", java.time.LocalDateTime.now().toString());
        return problemDetail;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(EntityNotFoundException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "The requested resource could not be found"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPasswordException(InvalidPasswordException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,  // Or another status, like FORBIDDEN
                ex.getMessage(),
                "The provided password does not match the existing password"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ProblemDetail> handleSQLException(SQLException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "A database error occurred",
                "An unexpected database error prevented the operation from completing"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder detailBuilder = new StringBuilder("Validation failed for fields: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detailBuilder.append(error.getField())
                    .append(" (")
                    .append(error.getDefaultMessage())
                    .append("), ");
        }
        String detail = detailBuilder.substring(0, detailBuilder.length() - 2);

        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                detail
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    //Security or login exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                "The username or password is incorrect"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ProblemDetail> handleAccountStatusException(AccountStatusException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "The account is locked or inactive"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "You are not authorized to access this resource"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ProblemDetail> handleSignatureException(SignatureException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "The JWT signature is invalid"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ProblemDetail> handleExpiredJwtException(ExpiredJwtException ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "The JWT token has expired"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        ProblemDetail errorDetail = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong!",
                "An unexpected error occurred. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetail);
    }
}
