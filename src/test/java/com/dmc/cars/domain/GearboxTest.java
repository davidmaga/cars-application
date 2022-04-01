package com.dmc.cars.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dmc.cars.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GearboxTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gearbox.class);
        Gearbox gearbox1 = new Gearbox();
        gearbox1.setId(1L);
        Gearbox gearbox2 = new Gearbox();
        gearbox2.setId(gearbox1.getId());
        assertThat(gearbox1).isEqualTo(gearbox2);
        gearbox2.setId(2L);
        assertThat(gearbox1).isNotEqualTo(gearbox2);
        gearbox1.setId(null);
        assertThat(gearbox1).isNotEqualTo(gearbox2);
    }
}
