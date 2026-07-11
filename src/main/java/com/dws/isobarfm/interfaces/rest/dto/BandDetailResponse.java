package com.dws.isobarfm.interfaces.rest.dto;

import java.util.List;

/**
 * Full band projection for the band-detail screen: metadata, biography and
 * the resolved album list.
 */
public class BandDetailResponse {

    private final String id;
    private final String name;
    private final String image;
    private final String genre;
    private final String biography;
    private final long numPlays;
    private final List<AlbumResponse> albums;

    public BandDetailResponse(String id, String name, String image, String genre,
                              String biography, long numPlays, List<AlbumResponse> albums) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genre = genre;
        this.biography = biography;
        this.numPlays = numPlays;
        this.albums = albums;
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

    public List<AlbumResponse> getAlbums() {
        return albums;
    }
}
