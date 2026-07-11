package com.dws.isobarfm.domain.repository;

import com.dws.isobarfm.domain.model.Album;

import java.util.Optional;

/**
 * Outbound port for retrieving albums by id. Implemented in the infrastructure
 * layer by the HTTP adapter to the DWS bands-api.
 */
public interface AlbumRepository {

    /** @return the album with the given id, if it exists. */
    Optional<Album> findById(String id);
}
