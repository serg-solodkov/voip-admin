package ru.solodkov.voipadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendorMapperTest {

    private VendorMapper vendorMapper;

    @BeforeEach
    public void setUp() {
        vendorMapper = new VendorMapperImpl();
    }
}
