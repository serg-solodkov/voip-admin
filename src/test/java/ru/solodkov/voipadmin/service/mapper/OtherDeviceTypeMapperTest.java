package ru.solodkov.voipadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtherDeviceTypeMapperTest {

    private OtherDeviceTypeMapper otherDeviceTypeMapper;

    @BeforeEach
    public void setUp() {
        otherDeviceTypeMapper = new OtherDeviceTypeMapperImpl();
    }
}
