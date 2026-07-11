package com.dws.isobarfm.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

/**
 * Wire model for an album as returned by the upstream bands-api. The upstream
 * payload also nests the owning band; we intentionally ignore it here.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumApiResponse {

    private String id;
    private String name;
    private Instant releasedDate;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Instant releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
