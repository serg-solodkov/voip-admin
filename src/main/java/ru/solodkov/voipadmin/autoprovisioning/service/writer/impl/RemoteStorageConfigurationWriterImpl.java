package ru.solodkov.voipadmin.autoprovisioning.service.writer.impl;

import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;

public class RemoteStorageConfigurationWriterImpl implements ConfigurationWriter {

    @Override
    public boolean writeConfig(ConfigurationFile configurationFile) {
        return false;
    }
}
