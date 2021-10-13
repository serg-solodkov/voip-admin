package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class AsteriskAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsteriskAccount.class);
        AsteriskAccount asteriskAccount1 = new AsteriskAccount();
        asteriskAccount1.setId(1L);
        AsteriskAccount asteriskAccount2 = new AsteriskAccount();
        asteriskAccount2.setId(asteriskAccount1.getId());
        assertThat(asteriskAccount1).isEqualTo(asteriskAccount2);
        asteriskAccount2.setId(2L);
        assertThat(asteriskAccount1).isNotEqualTo(asteriskAccount2);
        asteriskAccount1.setId(null);
        assertThat(asteriskAccount1).isNotEqualTo(asteriskAccount2);
    }
}
