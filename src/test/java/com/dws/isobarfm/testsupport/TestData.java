package com.dws.isobarfm.testsupport;

import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.model.Band;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Small builders to keep tests concise and readable. */
public final class TestData {

    private TestData() {
    }

    public static Band band(String id, String name, long numPlays, String... albumIds) {
        List<String> albums = albumIds.length == 0
                ? Collections.emptyList()
                : Arrays.asList(albumIds);
        return new Band(id, name, "http://img/" + id + ".png", "rock",
                name + " biography", numPlays, albums);
    }

    public static Album album(String id, String name) {
        return new Album(id, name, Instant.parse("2001-01-01T00:00:00Z"),
                "http://img/album-" + id + ".png");
    }
}
