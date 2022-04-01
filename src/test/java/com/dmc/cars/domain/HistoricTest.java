package com.dmc.cars.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dmc.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historic.class);
        Historic historic1 = new Historic();
        historic1.setId(1L);
        Historic historic2 = new Historic();
        historic2.setId(historic1.getId());
        assertThat(historic1).isEqualTo(historic2);
        historic2.setId(2L);
        assertThat(historic1).isNotEqualTo(historic2);
        historic1.setId(null);
        assertThat(historic1).isNotEqualTo(historic2);
    }
}
