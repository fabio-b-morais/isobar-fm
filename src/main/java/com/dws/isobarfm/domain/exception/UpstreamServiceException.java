package com.dws.isobarfm.domain.exception;

/**
 * Raised when the upstream bands-api cannot be reached or returns an error.
 * Maps to HTTP 502 (Bad Gateway).
 */
public class UpstreamServiceException extends RuntimeException {

    public UpstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
