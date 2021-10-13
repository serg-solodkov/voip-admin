package ru.solodkov.voipadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionValueMapperTest {

    private OptionValueMapper optionValueMapper;

    @BeforeEach
    public void setUp() {
        optionValueMapper = new OptionValueMapperImpl();
    }
}
