package com.dws.isobarfm.domain.repository;

import com.dws.isobarfm.domain.model.Band;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for retrieving bands. The domain/application layers depend on
 * this abstraction; the concrete adapter (HTTP client to the DWS bands-api)
 * lives in the infrastructure layer (Dependency Inversion Principle).
 */
public interface BandRepository {

    /** @return all bands known to the upstream source. */
    List<Band> findAll();

    /** @return the band with the given id, if it exists. */
    Optional<Band> findById(String id);
}
