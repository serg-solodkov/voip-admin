package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class OtherDeviceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtherDeviceType.class);
        OtherDeviceType otherDeviceType1 = new OtherDeviceType();
        otherDeviceType1.setId(1L);
        OtherDeviceType otherDeviceType2 = new OtherDeviceType();
        otherDeviceType2.setId(otherDeviceType1.getId());
        assertThat(otherDeviceType1).isEqualTo(otherDeviceType2);
        otherDeviceType2.setId(2L);
        assertThat(otherDeviceType1).isNotEqualTo(otherDeviceType2);
        otherDeviceType1.setId(null);
        assertThat(otherDeviceType1).isNotEqualTo(otherDeviceType2);
    }
}
