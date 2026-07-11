package com.dws.isobarfm.interfaces.rest.dto;

/**
 * List/search projection of a band (band-list &amp; search screens): enough to
 * render a row — image, name, genre and play count.
 */
public class BandSummaryResponse {

    private final String id;
    private final String name;
    private final String image;
    private final String genre;
    private final long numPlays;

    public BandSummaryResponse(String id, String name, String image, String genre, long numPlays) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genre = genre;
        this.numPlays = numPlays;
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

    public long getNumPlays() {
        return numPlays;
    }
}
