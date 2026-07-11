package com.dws.isobarfm.interfaces.rest;

import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.domain.model.BandDetail;
import com.dws.isobarfm.interfaces.rest.dto.AlbumResponse;
import com.dws.isobarfm.interfaces.rest.dto.BandDetailResponse;
import com.dws.isobarfm.interfaces.rest.dto.BandSummaryResponse;

import java.util.stream.Collectors;

/** Maps domain models to the REST response DTOs. */
final class BandRestMapper {

    private BandRestMapper() {
    }

    static BandSummaryResponse toSummary(Band band) {
        return new BandSummaryResponse(
                band.getId(),
                band.getName(),
                band.getImage(),
                band.getGenre(),
                band.getNumPlays());
    }

    static BandDetailResponse toDetail(BandDetail detail) {
        Band band = detail.getBand();
        return new BandDetailResponse(
                band.getId(),
                band.getName(),
                band.getImage(),
                band.getGenre(),
                band.getBiography(),
                band.getNumPlays(),
                detail.getAlbums().stream()
                        .map(BandRestMapper::toAlbum)
                        .collect(Collectors.toList()));
    }

    private static AlbumResponse toAlbum(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getName(),
                album.getImage(),
                album.getReleasedDate());
    }
}
