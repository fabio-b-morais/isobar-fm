package com.dws.isobarfm.interfaces.rest;

import com.dws.isobarfm.application.query.BandListQuery;
import com.dws.isobarfm.application.usecase.GetBandDetailUseCase;
import com.dws.isobarfm.application.usecase.ListBandsUseCase;
import com.dws.isobarfm.domain.model.BandSortField;
import com.dws.isobarfm.domain.model.SortDirection;
import com.dws.isobarfm.interfaces.rest.dto.BandDetailResponse;
import com.dws.isobarfm.interfaces.rest.dto.BandSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * REST entry point exposing the bands API consumed by the client apps.
 *
 * <ul>
 *   <li>{@code GET /api/v1/bands}          — list, search and sort (screens 1 &amp; 2)</li>
 *   <li>{@code GET /api/v1/bands/{id}}     — band detail with albums (screen 3)</li>
 * </ul>
 */
@Tag(name = "Bands", description = "List, search and detail endpoints for bands and their albums")
@RestController
@RequestMapping("/api/v1/bands")
public class BandController {

    private final ListBandsUseCase listBandsUseCase;
    private final GetBandDetailUseCase getBandDetailUseCase;

    public BandController(ListBandsUseCase listBandsUseCase,
                          GetBandDetailUseCase getBandDetailUseCase) {
        this.listBandsUseCase = listBandsUseCase;
        this.getBandDetailUseCase = getBandDetailUseCase;
    }

    /**
     * @param q     optional case-insensitive name filter (search screen)
     * @param sort  {@code name} (default) or {@code popularity}
     * @param order {@code asc} (default) or {@code desc}
     */
    @Operation(summary = "List bands with optional name filter and sorting (name/popularity)")
    @GetMapping
    public List<BandSummaryResponse> list(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {

        BandListQuery query = new BandListQuery(q, parseSort(sort), parseOrder(order));
        return listBandsUseCase.execute(query).stream()
                .map(BandRestMapper::toSummary)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a band's full detail, including its resolved albums")
    @GetMapping("/{id}")
    public BandDetailResponse detail(@PathVariable("id") String id) {
        return BandRestMapper.toDetail(getBandDetailUseCase.execute(id));
    }

    private BandSortField parseSort(String sort) {
        switch (sort.toLowerCase(Locale.ROOT)) {
            case "name":
                return BandSortField.NAME;
            case "popularity":
                return BandSortField.POPULARITY;
            default:
                throw new IllegalArgumentException(
                        "Invalid 'sort' value: '" + sort + "'. Allowed: name, popularity");
        }
    }

    private SortDirection parseOrder(String order) {
        switch (order.toLowerCase(Locale.ROOT)) {
            case "asc":
                return SortDirection.ASC;
            case "desc":
                return SortDirection.DESC;
            default:
                throw new IllegalArgumentException(
                        "Invalid 'order' value: '" + order + "'. Allowed: asc, desc");
        }
    }
}
