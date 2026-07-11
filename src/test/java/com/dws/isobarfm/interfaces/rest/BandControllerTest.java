package com.dws.isobarfm.interfaces.rest;

import com.dws.isobarfm.application.query.BandListQuery;
import com.dws.isobarfm.application.usecase.GetBandDetailUseCase;
import com.dws.isobarfm.application.usecase.ListBandsUseCase;
import com.dws.isobarfm.domain.exception.BandNotFoundException;
import com.dws.isobarfm.domain.model.BandDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static com.dws.isobarfm.testsupport.TestData.album;
import static com.dws.isobarfm.testsupport.TestData.band;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BandController.class)
class BandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListBandsUseCase listBandsUseCase;

    @MockBean
    private GetBandDetailUseCase getBandDetailUseCase;

    @Test
    void listReturnsBandSummaries() throws Exception {
        when(listBandsUseCase.execute(any(BandListQuery.class))).thenReturn(Arrays.asList(
                band("1", "Radiohead", 553_212),
                band("2", "Pink Floyd", 284_212)));

        mockMvc.perform(get("/api/v1/bands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Radiohead"))
                .andExpect(jsonPath("$[0].numPlays").value(553_212));
    }

    @Test
    void listRejectsInvalidSortWith400() throws Exception {
        mockMvc.perform(get("/api/v1/bands").param("sort", "bogus"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void detailReturnsBandWithAlbums() throws Exception {
        BandDetail detail = new BandDetail(
                band("1", "Nickelback", 100, "a1"),
                Collections.singletonList(album("a1", "Silver Side Up")));
        when(getBandDetailUseCase.execute(eq("1"))).thenReturn(detail);

        mockMvc.perform(get("/api/v1/bands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nickelback"))
                .andExpect(jsonPath("$.albums.length()").value(1))
                .andExpect(jsonPath("$.albums[0].name").value("Silver Side Up"));
    }

    @Test
    void detailReturns404WhenBandMissing() throws Exception {
        when(getBandDetailUseCase.execute(eq("nope")))
                .thenThrow(new BandNotFoundException("nope"));

        mockMvc.perform(get("/api/v1/bands/nope"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
