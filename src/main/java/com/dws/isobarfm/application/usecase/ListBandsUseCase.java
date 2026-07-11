package com.dws.isobarfm.application.usecase;

import com.dws.isobarfm.application.query.BandListQuery;
import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.domain.model.BandSortField;
import com.dws.isobarfm.domain.model.SortDirection;
import com.dws.isobarfm.domain.repository.BandRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Lists bands (band-list &amp; search screens): optional name filter, sorted by
 * name or popularity. Depends only on the {@link BandRepository} port.
 */
@Service
public class ListBandsUseCase {

    private final BandRepository bandRepository;

    public ListBandsUseCase(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public List<Band> execute(BandListQuery query) {
        return bandRepository.findAll().stream()
                .filter(band -> matchesSearch(band, query))
                .sorted(comparatorFor(query))
                .collect(Collectors.toList());
    }

    private boolean matchesSearch(Band band, BandListQuery query) {
        if (!query.hasSearch()) {
            return true;
        }
        String needle = query.getSearch().toLowerCase(Locale.ROOT);
        return band.getName().toLowerCase(Locale.ROOT).contains(needle);
    }

    private Comparator<Band> comparatorFor(BandListQuery query) {
        Comparator<Band> comparator = query.getSortField() == BandSortField.POPULARITY
                ? Comparator.comparingLong(Band::getNumPlays)
                : Comparator.comparing(Band::getName, String.CASE_INSENSITIVE_ORDER);
        return query.getDirection() == SortDirection.DESC ? comparator.reversed() : comparator;
    }
}
