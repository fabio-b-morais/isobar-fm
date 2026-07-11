package com.dws.isobarfm.application.usecase;

import com.dws.isobarfm.domain.exception.BandNotFoundException;
import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.model.BandDetail;
import com.dws.isobarfm.domain.repository.AlbumRepository;
import com.dws.isobarfm.domain.repository.BandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.dws.isobarfm.testsupport.TestData.album;
import static com.dws.isobarfm.testsupport.TestData.band;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetBandDetailUseCaseTest {

    private BandRepository bandRepository;
    private AlbumRepository albumRepository;
    private GetBandDetailUseCase useCase;

    @BeforeEach
    void setUp() {
        bandRepository = mock(BandRepository.class);
        albumRepository = mock(AlbumRepository.class);
        useCase = new GetBandDetailUseCase(bandRepository, albumRepository);
    }

    @Test
    void resolvesBandWithItsAlbums() {
        when(bandRepository.findById("1")).thenReturn(Optional.of(band("1", "Nickelback", 100, "a1", "a2")));
        when(albumRepository.findById("a1")).thenReturn(Optional.of(album("a1", "Silver Side Up")));
        when(albumRepository.findById("a2")).thenReturn(Optional.of(album("a2", "The Long Road")));

        BandDetail detail = useCase.execute("1");

        assertThat(detail.getBand().getName()).isEqualTo("Nickelback");
        assertThat(detail.getAlbums().stream().map(Album::getName).collect(Collectors.toList()))
                .containsExactly("Silver Side Up", "The Long Road");
    }

    @Test
    void skipsAlbumsThatCannotBeResolved() {
        when(bandRepository.findById("1")).thenReturn(Optional.of(band("1", "Nickelback", 100, "a1", "missing")));
        when(albumRepository.findById("a1")).thenReturn(Optional.of(album("a1", "Silver Side Up")));
        when(albumRepository.findById("missing")).thenReturn(Optional.empty());

        BandDetail detail = useCase.execute("1");

        assertThat(detail.getAlbums()).hasSize(1);
        assertThat(detail.getAlbums().get(0).getName()).isEqualTo("Silver Side Up");
    }

    @Test
    void throwsWhenBandDoesNotExist() {
        when(bandRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("nope"))
                .isInstanceOf(BandNotFoundException.class)
                .hasMessageContaining("nope");
    }
}
