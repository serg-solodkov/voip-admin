package ru.solodkov.voipadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsteriskAccountMapperTest {

    private AsteriskAccountMapper asteriskAccountMapper;

    @BeforeEach
    public void setUp() {
        asteriskAccountMapper = new AsteriskAccountMapperImpl();
    }
}
