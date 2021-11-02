package ru.solodkov.voipadmin.autoprovisioning.service.writer.impl;

import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;

@Service
public class LocalStorageConfigurationWriter implements ConfigurationWriter {

    @Override
    public boolean writeConfig(ConfigurationFile configurationFile) {
        return false;
    }
}
