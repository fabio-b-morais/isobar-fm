package com.dws.isobarfm.application.usecase;

import com.dws.isobarfm.application.query.BandListQuery;
import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.domain.model.BandSortField;
import com.dws.isobarfm.domain.model.SortDirection;
import com.dws.isobarfm.domain.repository.BandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dws.isobarfm.testsupport.TestData.band;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListBandsUseCaseTest {

    private BandRepository bandRepository;
    private ListBandsUseCase useCase;

    @BeforeEach
    void setUp() {
        bandRepository = mock(BandRepository.class);
        useCase = new ListBandsUseCase(bandRepository);

        when(bandRepository.findAll()).thenReturn(Arrays.asList(
                band("1", "Radiohead", 553_212),
                band("2", "The Beatles", 475_270),
                band("3", "Pink Floyd", 284_212),
                band("4", "ABBA", 100_000)));
    }

    @Test
    void sortsAlphabeticallyAscendingByDefault() {
        List<Band> result = useCase.execute(BandListQuery.defaults());

        assertThat(names(result)).containsExactly("ABBA", "Pink Floyd", "Radiohead", "The Beatles");
    }

    @Test
    void sortsAlphabeticallyDescending() {
        List<Band> result = useCase.execute(
                new BandListQuery(null, BandSortField.NAME, SortDirection.DESC));

        assertThat(names(result)).containsExactly("The Beatles", "Radiohead", "Pink Floyd", "ABBA");
    }

    @Test
    void sortsByPopularityDescending() {
        List<Band> result = useCase.execute(
                new BandListQuery(null, BandSortField.POPULARITY, SortDirection.DESC));

        assertThat(names(result)).containsExactly("Radiohead", "The Beatles", "Pink Floyd", "ABBA");
    }

    @Test
    void filtersByNameCaseInsensitively() {
        List<Band> result = useCase.execute(
                new BandListQuery("pink", BandSortField.NAME, SortDirection.ASC));

        assertThat(names(result)).containsExactly("Pink Floyd");
    }

    @Test
    void returnsEmptyWhenNoMatch() {
        List<Band> result = useCase.execute(
                new BandListQuery("zzz", null, null));

        assertThat(result).isEmpty();
    }

    private static List<String> names(List<Band> bands) {
        return bands.stream().map(Band::getName).collect(Collectors.toList());
    }
}
