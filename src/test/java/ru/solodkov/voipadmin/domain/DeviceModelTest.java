package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class DeviceModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceModel.class);
        DeviceModel deviceModel1 = new DeviceModel();
        deviceModel1.setId(1L);
        DeviceModel deviceModel2 = new DeviceModel();
        deviceModel2.setId(deviceModel1.getId());
        assertThat(deviceModel1).isEqualTo(deviceModel2);
        deviceModel2.setId(2L);
        assertThat(deviceModel1).isNotEqualTo(deviceModel2);
        deviceModel1.setId(null);
        assertThat(deviceModel1).isNotEqualTo(deviceModel2);
    }
}
