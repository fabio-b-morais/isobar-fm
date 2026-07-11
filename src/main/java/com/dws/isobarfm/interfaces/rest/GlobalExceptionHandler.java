package com.dws.isobarfm.interfaces.rest;

import com.dws.isobarfm.domain.exception.BandNotFoundException;
import com.dws.isobarfm.domain.exception.UpstreamServiceException;
import com.dws.isobarfm.interfaces.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Translates exceptions into consistent HTTP responses. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BandNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(BandNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleUpstream(UpstreamServiceException ex) {
        return build(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(body);
    }
}
