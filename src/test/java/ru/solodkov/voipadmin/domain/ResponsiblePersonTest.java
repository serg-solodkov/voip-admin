package ru.solodkov.voipadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.solodkov.voipadmin.web.rest.TestUtil;

class ResponsiblePersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponsiblePerson.class);
        ResponsiblePerson responsiblePerson1 = new ResponsiblePerson();
        responsiblePerson1.setId(1L);
        ResponsiblePerson responsiblePerson2 = new ResponsiblePerson();
        responsiblePerson2.setId(responsiblePerson1.getId());
        assertThat(responsiblePerson1).isEqualTo(responsiblePerson2);
        responsiblePerson2.setId(2L);
        assertThat(responsiblePerson1).isNotEqualTo(responsiblePerson2);
        responsiblePerson1.setId(null);
        assertThat(responsiblePerson1).isNotEqualTo(responsiblePerson2);
    }
}
