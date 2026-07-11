# isobar.fm — DWS Backend Java Test

A Java/Spring Boot backend that consumes the DWS bands API
(`https://bands-api.vercel.app/api`) and re-exposes it as a clean REST API with
a **caching layer**, built with **DDD + Clean Architecture + SOLID** and covered
by **unit tests**.

> **Live on AWS:** `https://<SERVICE_URL>`
> Jump to [Testing the live API on AWS](#-testing-the-live-api-on-aws-curl) for a copy‑paste curl walkthrough.

---

## What it does (mapped to the screens)

| Screen | Behaviour | Endpoint |
|---|---|---|
| List of results with an **alphabetical / popularity** toggle | list + sort | `GET /api/v1/bands?sort=name\|popularity&order=asc\|desc` |
| Search "pink" | filter by name | `GET /api/v1/bands?q=pink` |
| Band detail (Truckfighters) with **albums** | detail + resolved albums | `GET /api/v1/bands/{id}` |

The upstream band object only references albums by id, so the detail endpoint
resolves each album against the upstream `/albums/{id}` endpoint and returns the
enriched aggregate.

### API docs (Swagger UI)

Interactive docs are generated with springdoc/OpenAPI 3:

| | Local | AWS |
|---|---|---|
| Swagger UI  | http://localhost:8080/swagger-ui.html | `https://<SERVICE_URL>/swagger-ui.html` |
| OpenAPI JSON | http://localhost:8080/v3/api-docs     | `https://<SERVICE_URL>/v3/api-docs` |

---

## Architecture

Dependencies point **inwards** (Clean Architecture). The domain has zero
framework/Spring imports.

```
com.dws.isobarfm
├── domain            # Enterprise rules — pure Java, no Spring
│   ├── model         #   Band, Album, BandDetail, BandSortField, SortDirection
│   ├── repository    #   Ports: BandRepository, AlbumRepository  (interfaces)
│   └── exception     #   BandNotFoundException, UpstreamServiceException
├── application       # Use cases (application rules)
│   ├── usecase       #   ListBandsUseCase, GetBandDetailUseCase
│   └── query         #   BandListQuery (filter/sort params)
├── infrastructure    # Frameworks & drivers
│   ├── client        #   BandsApiClient (RestTemplate) + wire DTOs + mapper
│   ├── adapter       #   *HttpAdapter — implement the ports, apply @Cacheable
│   └── config        #   CacheConfig (Caffeine), RestClientConfig, properties
└── interfaces        # Presentation
    └── rest          #   BandController, response DTOs, GlobalExceptionHandler
```

**How the principles show up**
- **DDD:** `domain` holds entities/aggregates (`BandDetail`) and repository
  *ports*; the language (Band, Album, popularity) matches the business.
- **Clean Architecture:** the upstream API is an implementation detail behind the
  `BandRepository`/`AlbumRepository` ports — swapping the data source touches only
  `infrastructure`.
- **SOLID:** each use case has a single responsibility; new list options extend
  `BandListQuery` without changing signatures (OCP); high-level code depends on
  port abstractions, not the HTTP client (DIP).

---

## Caching layer

Uses **Caffeine** behind Spring's cache abstraction (`@EnableCaching` +
`@Cacheable`). Upstream responses are cached in memory for a configurable TTL
(default 10 min), so the third‑party API is hit at most once per key per window.

- Caches: `bands` (full list), `band` (by id), `album` (by id).
- Applied at the **adapter** layer, so business code stays cache‑agnostic.
- Configured in `CacheConfig`; TTL/size via `application.yml` (`cache.*`).

Because it goes through the `CacheManager` abstraction, moving to a distributed
cache (**Redis / AWS ElastiCache**) later is a config change, not a code change.

Quick proof (second call served from memory):
```
call1: 0.026s   <- upstream fetched + cached
call2: 0.003s   <- served from Caffeine
```

---

## Tech stack

- Java 11, Spring Boot 2.7.18 (last 2.7 line supporting Java 11)
- Spring Web, Spring Cache + Caffeine, Actuator (health), Bean Validation
- springdoc-openapi (Swagger UI / OpenAPI 3)
- JUnit 5, Mockito, AssertJ, Spring MockMvc — **14 unit tests**
- Maven, Docker, AWS App Runner + ECR

---

## Run locally

```bash
# requires JDK 11 + Maven
mvn clean test          # run the 14 unit tests
mvn spring-boot:run     # start on http://localhost:8080
# or:
mvn -DskipTests package && java -jar target/isobar-fm.jar
```

### curl (local)

```bash
BASE=http://localhost:8080

# List (alphabetical asc by default)
curl "$BASE/api/v1/bands"

# Sort by popularity, most played first
curl "$BASE/api/v1/bands?sort=popularity&order=desc"

# Search by name (case-insensitive)
curl "$BASE/api/v1/bands?q=bon"

# Band detail with albums (grab an id from the list first)
curl "$BASE/api/v1/bands/5dcdb5eb-cb72-4e6e-9e63-b7bace604965"

# Health
curl "$BASE/actuator/health"
```

---

## Deploy to AWS (App Runner + ECR)

Prereqs: `aws configure` done, Docker running. One command:

```bash
bash deploy/deploy-apprunner.sh
```

It builds the image, pushes to ECR, ensures the App Runner ECR access role, and
creates/redeploys the service, printing the public HTTPS URL when `RUNNING`.

---

## 🔎 Testing the live API on AWS (curl)

Replace `<SERVICE_URL>` with the deployed host (also shown at the top).

```bash
BASE=https://<SERVICE_URL>

# 1) Health check
curl "$BASE/actuator/health"

# 2) List all bands — alphabetical (screen 1, alphabetical toggle)
curl "$BASE/api/v1/bands"

# 3) Sort by popularity, most played first (screen 1, popularity toggle)
curl "$BASE/api/v1/bands?sort=popularity&order=desc"

# 4) Search by name (screen 2)
curl "$BASE/api/v1/bands?q=bon"

# 5) Band detail with albums (screen 3) — use an id from step 2/4
curl "$BASE/api/v1/bands/5dcdb5eb-cb72-4e6e-9e63-b7bace604965"

# 6) Error handling
curl -i "$BASE/api/v1/bands?sort=bogus"    # 400 Bad Request
curl -i "$BASE/api/v1/bands/nope"          # 404 Not Found
```

Query parameters for `GET /api/v1/bands`:

| Param  | Values                    | Default | Meaning                     |
|--------|---------------------------|---------|-----------------------------|
| `q`    | any text                  | –       | case-insensitive name filter |
| `sort` | `name`, `popularity`      | `name`  | ordering field              |
| `order`| `asc`, `desc`             | `asc`   | ordering direction          |
