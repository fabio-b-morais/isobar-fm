package com.dws.isobarfm.infrastructure.adapter;

import com.dws.isobarfm.domain.model.Album;
import com.dws.isobarfm.domain.repository.AlbumRepository;
import com.dws.isobarfm.infrastructure.client.BandsApiClient;
import com.dws.isobarfm.infrastructure.client.BandsApiMapper;
import com.dws.isobarfm.infrastructure.config.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Infrastructure adapter satisfying the {@link AlbumRepository} port. Album
 * lookups are cached per id, so resolving a band's albums hits the upstream API
 * at most once per album per TTL window.
 */
@Repository
public class AlbumRepositoryHttpAdapter implements AlbumRepository {

    private final BandsApiClient client;

    public AlbumRepositoryHttpAdapter(BandsApiClient client) {
        this.client = client;
    }

    @Override
    @Cacheable(value = CacheConfig.ALBUM_CACHE, key = "#id")
    public Optional<Album> findById(String id) {
        return client.getAlbumById(id).map(BandsApiMapper::toDomain);
    }
}
