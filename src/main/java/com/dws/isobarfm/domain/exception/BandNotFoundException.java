package com.dws.isobarfm.domain.exception;

/** Raised when a band with the requested id does not exist. Maps to HTTP 404. */
public class BandNotFoundException extends RuntimeException {

    public BandNotFoundException(String id) {
        super("Band not found: " + id);
    }
}
