package ru.solodkov.voipadmin.autoprovisioning.service.writer.impl;

import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;

public class NoOpConfigurationWriterImpl implements ConfigurationWriter {

    @Override
    public boolean writeConfig(ConfigurationFile configurationFile) {
        return false;
    }
}
