package com.voipadmin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.voipadmin.web.rest.TestUtil;

public class SettingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SettingDTO.class);
        SettingDTO settingDTO1 = new SettingDTO();
        settingDTO1.setId(1L);
        SettingDTO settingDTO2 = new SettingDTO();
        assertThat(settingDTO1).isNotEqualTo(settingDTO2);
        settingDTO2.setId(settingDTO1.getId());
        assertThat(settingDTO1).isEqualTo(settingDTO2);
        settingDTO2.setId(2L);
        assertThat(settingDTO1).isNotEqualTo(settingDTO2);
        settingDTO1.setId(null);
        assertThat(settingDTO1).isNotEqualTo(settingDTO2);
    }
}
