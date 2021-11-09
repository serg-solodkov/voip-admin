package ru.solodkov.voipadmin.autoprovisioning.service.writer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class LocalStorageConfigurationWriterImpl implements ConfigurationWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalStorageConfigurationWriterImpl.class);

    @Value("${provisioning.ftp.location:${provisioning.tftp.location:/tmp/voip-admin}}")
    private String location;

    @Override
    public boolean writeConfig(ConfigurationFile configurationFile) {
        Path configFilesDirPath = Path.of(location);
        if (Files.notExists(configFilesDirPath)) {
            try {
                Files.createDirectories(configFilesDirPath);
            } catch (IOException ex) {
                LOGGER.error("Error while trying to create directories: " + configFilesDirPath);
                LOGGER.error(ex.getMessage());
                LOGGER.error(ex.getCause().toString());
                return false;
            }
        }

        Path configFilePath = configFilesDirPath.resolve(configurationFile.getFileName());
        try {
            Files.write(configFilePath, configurationFile.getContent());
            return true;
        } catch (IOException ex) {
            LOGGER.error("Error while trying to write configuration file: " + configFilePath);
            LOGGER.error(ex.getMessage());
            LOGGER.error(ex.getCause().toString());
            return false;
        }
    }
}
