package com.dws.isobarfm.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Core domain entity representing an album. Identity is defined by {@link #id}.
 */
public final class Album {

    private final String id;
    private final String name;
    private final Instant releasedDate;
    private final String image;

    public Album(String id, String name, Instant releasedDate, String image) {
        this.id = Objects.requireNonNull(id, "album id must not be null");
        this.name = Objects.requireNonNull(name, "album name must not be null");
        this.releasedDate = releasedDate;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getReleasedDate() {
        return releasedDate;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        return id.equals(((Album) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Album{id='" + id + "', name='" + name + "'}";
    }
}
