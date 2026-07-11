package com.dws.isobarfm.interfaces.rest.dto;

import java.time.Instant;

/** Album projection used inside the band-detail response. */
public class AlbumResponse {

    private final String id;
    private final String name;
    private final String image;
    private final Instant releasedDate;

    public AlbumResponse(String id, String name, String image, Instant releasedDate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.releasedDate = releasedDate;
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

    public Instant getReleasedDate() {
        return releasedDate;
    }
}
