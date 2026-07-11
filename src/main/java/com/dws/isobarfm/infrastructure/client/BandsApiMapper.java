package com.dws.isobarfm.infrastructure.client;

import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.infrastructure.client.dto.AlbumApiResponse;
import com.dws.isobarfm.infrastructure.client.dto.BandApiResponse;

/**
 * Translates upstream wire models into domain entities, keeping Jackson/HTTP
 * concerns out of the domain (Anti-Corruption Layer).
 */
public final class BandsApiMapper {

    private BandsApiMapper() {
    }

    public static Band toDomain(BandApiResponse dto) {
        return new Band(
                dto.getId(),
                dto.getName(),
                dto.getImage(),
                dto.getGenre(),
                dto.getBiography(),
                dto.getNumPlays(),
                dto.getAlbums());
    }

    public static Album toDomain(AlbumApiResponse dto) {
        return new Album(
                dto.getId(),
                dto.getName(),
                dto.getReleasedDate(),
                dto.getImage());
    }
}
