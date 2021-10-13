package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class VoipAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoipAccount.class);
        VoipAccount voipAccount1 = new VoipAccount();
        voipAccount1.setId(1L);
        VoipAccount voipAccount2 = new VoipAccount();
        voipAccount2.setId(voipAccount1.getId());
        assertThat(voipAccount1).isEqualTo(voipAccount2);
        voipAccount2.setId(2L);
        assertThat(voipAccount1).isNotEqualTo(voipAccount2);
        voipAccount1.setId(null);
        assertThat(voipAccount1).isNotEqualTo(voipAccount2);
    }
}
