package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class VendorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        Vendor vendor2 = new Vendor();
        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);
        vendor2.setId(2L);
        assertThat(vendor1).isNotEqualTo(vendor2);
        vendor1.setId(null);
        assertThat(vendor1).isNotEqualTo(vendor2);
    }
}
