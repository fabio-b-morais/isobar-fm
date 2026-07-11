package com.dws.isobarfm.application.query;

import com.dws.isobarfm.domain.model.BandSortField;
import com.dws.isobarfm.domain.model.SortDirection;

/**
 * Immutable query object describing how the band list should be filtered and
 * ordered. Encapsulating these parameters keeps the use-case signature stable
 * as new options are added (Open/Closed Principle).
 */
public final class BandListQuery {

    private final String search;
    private final BandSortField sortField;
    private final SortDirection direction;

    public BandListQuery(String search, BandSortField sortField, SortDirection direction) {
        // Blank search means "no filter".
        this.search = (search == null || search.trim().isEmpty()) ? null : search.trim();
        this.sortField = sortField == null ? BandSortField.NAME : sortField;
        this.direction = direction == null ? SortDirection.ASC : direction;
    }

    /** Sensible default: alphabetical, ascending, no filter. */
    public static BandListQuery defaults() {
        return new BandListQuery(null, BandSortField.NAME, SortDirection.ASC);
    }

    public boolean hasSearch() {
        return search != null;
    }

    public String getSearch() {
        return search;
    }

    public BandSortField getSortField() {
        return sortField;
    }

    public SortDirection getDirection() {
        return direction;
    }
}
