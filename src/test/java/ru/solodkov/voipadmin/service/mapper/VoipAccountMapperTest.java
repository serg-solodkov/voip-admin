package ru.solodkov.voipadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoipAccountMapperTest {

    private VoipAccountMapper voipAccountMapper;

    @BeforeEach
    public void setUp() {
        voipAccountMapper = new VoipAccountMapperImpl();
    }
}
