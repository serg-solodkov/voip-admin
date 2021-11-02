package ru.solodkov.voipadmin.autoprovisioning.service.builder;

import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.domain.Device;

public interface ConfigurationBuilder {

    ConfigurationFile buildConfig(Device device);
}
