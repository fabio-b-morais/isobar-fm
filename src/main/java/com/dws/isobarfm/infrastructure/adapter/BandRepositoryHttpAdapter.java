package com.dws.isobarfm.infrastructure.adapter;

import com.dws.isobarfm.domain.model.Band;
import com.dws.isobarfm.domain.repository.BandRepository;
import com.dws.isobarfm.infrastructure.client.BandsApiClient;
import com.dws.isobarfm.infrastructure.client.BandsApiMapper;
import com.dws.isobarfm.infrastructure.config.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** {@link BandRepository} adapter over the upstream API, with cached ({@code @Cacheable}) reads. */
@Repository
public class BandRepositoryHttpAdapter implements BandRepository {

    private final BandsApiClient client;

    public BandRepositoryHttpAdapter(BandsApiClient client) {
        this.client = client;
    }

    @Override
    @Cacheable(CacheConfig.BANDS_CACHE)
    public List<Band> findAll() {
        return client.getBands().stream()
                .map(BandsApiMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheConfig.BAND_CACHE, key = "#id")
    public Optional<Band> findById(String id) {
        return client.getBandById(id).map(BandsApiMapper::toDomain);
    }
}
