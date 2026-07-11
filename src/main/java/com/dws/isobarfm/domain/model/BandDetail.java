package com.dws.isobarfm.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate combining a {@link Band} with its fully-resolved {@link Album}s.
 *
 * <p>Backs the band-detail screen (name, genre, plays, biography + albums).
 */
public final class BandDetail {

    private final Band band;
    private final List<Album> albums;

    public BandDetail(Band band, List<Album> albums) {
        this.band = Objects.requireNonNull(band, "band must not be null");
        this.albums = albums == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(albums);
    }

    public Band getBand() {
        return band;
    }

    public List<Album> getAlbums() {
        return albums;
    }
}
