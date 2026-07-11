package com.dws.isobarfm.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Core domain entity representing a music band.
 *
 * <p>Immutable and framework-agnostic: it carries no persistence, serialization
 * or HTTP concerns. Identity is defined solely by {@link #id}.
 */
public final class Band {

    private final String id;
    private final String name;
    private final String image;
    private final String genre;
    private final String biography;
    private final long numPlays;
    private final List<String> albumIds;

    public Band(String id, String name, String image, String genre,
                String biography, long numPlays, List<String> albumIds) {
        this.id = Objects.requireNonNull(id, "band id must not be null");
        this.name = Objects.requireNonNull(name, "band name must not be null");
        this.image = image;
        this.genre = genre;
        this.biography = biography;
        this.numPlays = numPlays;
        this.albumIds = albumIds == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(albumIds);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getGenre() {
        return genre;
    }

    public String getBiography() {
        return biography;
    }

    public long getNumPlays() {
        return numPlays;
    }

    public List<String> getAlbumIds() {
        return albumIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Band)) {
            return false;
        }
        return id.equals(((Band) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Band{id='" + id + "', name='" + name + "', numPlays=" + numPlays + '}';
    }
}
