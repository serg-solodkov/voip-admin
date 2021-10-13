package ru.solodkov.voipadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class OtherDeviceTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtherDeviceTypeDTO.class);
        OtherDeviceTypeDTO otherDeviceTypeDTO1 = new OtherDeviceTypeDTO();
        otherDeviceTypeDTO1.setId(1L);
        OtherDeviceTypeDTO otherDeviceTypeDTO2 = new OtherDeviceTypeDTO();
        assertThat(otherDeviceTypeDTO1).isNotEqualTo(otherDeviceTypeDTO2);
        otherDeviceTypeDTO2.setId(otherDeviceTypeDTO1.getId());
        assertThat(otherDeviceTypeDTO1).isEqualTo(otherDeviceTypeDTO2);
        otherDeviceTypeDTO2.setId(2L);
        assertThat(otherDeviceTypeDTO1).isNotEqualTo(otherDeviceTypeDTO2);
        otherDeviceTypeDTO1.setId(null);
        assertThat(otherDeviceTypeDTO1).isNotEqualTo(otherDeviceTypeDTO2);
    }
}
