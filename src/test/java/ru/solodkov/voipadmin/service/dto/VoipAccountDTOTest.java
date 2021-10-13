package ru.solodkov.voipadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class VoipAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoipAccountDTO.class);
        VoipAccountDTO voipAccountDTO1 = new VoipAccountDTO();
        voipAccountDTO1.setId(1L);
        VoipAccountDTO voipAccountDTO2 = new VoipAccountDTO();
        assertThat(voipAccountDTO1).isNotEqualTo(voipAccountDTO2);
        voipAccountDTO2.setId(voipAccountDTO1.getId());
        assertThat(voipAccountDTO1).isEqualTo(voipAccountDTO2);
        voipAccountDTO2.setId(2L);
        assertThat(voipAccountDTO1).isNotEqualTo(voipAccountDTO2);
        voipAccountDTO1.setId(null);
        assertThat(voipAccountDTO1).isNotEqualTo(voipAccountDTO2);
    }
}
