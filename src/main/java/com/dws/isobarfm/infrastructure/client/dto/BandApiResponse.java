package com.dws.isobarfm.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/** Wire model for a band as returned by the upstream bands-api. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BandApiResponse {

    private String id;
    private String name;
    private String image;
    private String genre;
    private String biography;
    private long numPlays;
    private List<String> albums;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public long getNumPlays() {
        return numPlays;
    }

    public void setNumPlays(long numPlays) {
        this.numPlays = numPlays;
    }

    public List<String> getAlbums() {
        return albums;
    }

    public void setAlbums(List<String> albums) {
        this.albums = albums;
    }
}
