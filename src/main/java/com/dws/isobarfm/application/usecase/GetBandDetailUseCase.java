package com.dws.isobarfm.application.usecase;

import com.dws.isobarfm.domain.exception.BandNotFoundException;
import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.domain.model.BandDetail;
import com.dws.isobarfm.domain.repository.AlbumRepository;
import com.dws.isobarfm.domain.repository.BandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Use case behind the band-detail screen. A band exposes only album ids, so
 * this use case resolves each id into a full {@link Album} through the
 * {@link AlbumRepository} port and assembles a {@link BandDetail} aggregate.
 */
@Service
public class GetBandDetailUseCase {

    private final BandRepository bandRepository;
    private final AlbumRepository albumRepository;

    public GetBandDetailUseCase(BandRepository bandRepository, AlbumRepository albumRepository) {
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
    }

    public BandDetail execute(String bandId) {
        Band band = bandRepository.findById(bandId)
                .orElseThrow(() -> new BandNotFoundException(bandId));

        List<Album> albums = band.getAlbumIds().stream()
                .map(albumRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new BandDetail(band, albums);
    }
}
