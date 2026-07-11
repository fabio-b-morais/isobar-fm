package com.dws.isobarfm.infrastructure.client;

import com.dws.isobarfm.domain.exception.UpstreamServiceException;
import com.dws.isobarfm.infrastructure.client.dto.AlbumApiResponse;
import com.dws.isobarfm.infrastructure.client.dto.BandApiResponse;
import com.dws.isobarfm.infrastructure.config.BandsApiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Thin HTTP client over the upstream DWS bands-api. Owns endpoint URLs and
 * error translation; it does not cache (caching lives in the repository
 * adapters) nor map to domain beyond delegating to {@link BandsApiMapper}.
 */
@Component
public class BandsApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BandsApiClient(RestTemplate bandsApiRestTemplate, BandsApiProperties properties) {
        this.restTemplate = bandsApiRestTemplate;
        this.baseUrl = properties.getBaseUrl();
    }

    public List<BandApiResponse> getBands() {
        try {
            BandApiResponse[] body = restTemplate.getForObject(baseUrl + "/bands", BandApiResponse[].class);
            return body == null ? Collections.emptyList() : Arrays.asList(body);
        } catch (RestClientException ex) {
            throw new UpstreamServiceException("Failed to fetch bands from upstream", ex);
        }
    }

    public Optional<BandApiResponse> getBandById(String id) {
        try {
            return Optional.ofNullable(
                    restTemplate.getForObject(baseUrl + "/bands/{id}", BandApiResponse.class, id));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        } catch (RestClientException ex) {
            throw new UpstreamServiceException("Failed to fetch band " + id + " from upstream", ex);
        }
    }

    public Optional<AlbumApiResponse> getAlbumById(String id) {
        try {
            return Optional.ofNullable(
                    restTemplate.getForObject(baseUrl + "/albums/{id}", AlbumApiResponse.class, id));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        } catch (RestClientException ex) {
            throw new UpstreamServiceException("Failed to fetch album " + id + " from upstream", ex);
        }
    }
}
