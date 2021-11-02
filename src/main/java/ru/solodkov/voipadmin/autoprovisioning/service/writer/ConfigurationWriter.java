package ru.solodkov.voipadmin.autoprovisioning.service.writer;

import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;

public interface ConfigurationWriter {

    boolean writeConfig(ConfigurationFile configurationFile);
}
