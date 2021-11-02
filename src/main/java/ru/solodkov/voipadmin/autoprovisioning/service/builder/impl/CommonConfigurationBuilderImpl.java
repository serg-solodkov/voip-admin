package ru.solodkov.voipadmin.autoprovisioning.service.builder.impl;

import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.ConfigurationBuilder;
import ru.solodkov.voipadmin.domain.Device;

@Service
public class CommonConfigurationBuilderImpl implements ConfigurationBuilder {

    @Override
    public ConfigurationFile buildConfig(Device device) {
        return null;
    }
}
