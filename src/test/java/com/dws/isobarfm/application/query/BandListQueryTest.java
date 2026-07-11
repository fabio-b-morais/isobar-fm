package com.dws.isobarfm.application.query;

import com.dws.isobarfm.domain.model.BandSortField;
import com.dws.isobarfm.domain.model.SortDirection;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BandListQueryTest {

    @Test
    void appliesDefaultsForNullArguments() {
        BandListQuery query = new BandListQuery(null, null, null);

        assertThat(query.hasSearch()).isFalse();
        assertThat(query.getSortField()).isEqualTo(BandSortField.NAME);
        assertThat(query.getDirection()).isEqualTo(SortDirection.ASC);
    }

    @Test
    void treatsBlankSearchAsNoFilterAndTrims() {
        assertThat(new BandListQuery("   ", null, null).hasSearch()).isFalse();
        assertThat(new BandListQuery("  pink  ", null, null).getSearch()).isEqualTo("pink");
    }
}
