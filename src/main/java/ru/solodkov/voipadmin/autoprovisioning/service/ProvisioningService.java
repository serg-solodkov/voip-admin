package ru.solodkov.voipadmin.autoprovisioning.service;

import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.ConfigurationBuilder;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.ConfigurationBuilderHolder;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;
import ru.solodkov.voipadmin.domain.Device;
import ru.solodkov.voipadmin.domain.enumeration.ProvisioningMode;

import static java.util.Objects.isNull;

@Service
public class ProvisioningService {

    private final ConfigurationBuilderHolder configurationBuilderHolder;
    private final ConfigurationWriter configurationWriter;

    public ProvisioningService(
        ConfigurationBuilderHolder configurationBuilderHolder, ConfigurationWriter configurationWriter
    ) {
        this.configurationBuilderHolder = configurationBuilderHolder;
        this.configurationWriter = configurationWriter;
    }

    public ConfigurationFile provide(Device device) {
        if (isNull(device.getModel()) || isNull(device.getModel().getVendor())) {
            return null;
        }
        String vendorName = device.getModel().getVendor().getName();
        if (isNull(vendorName)) {
            return null;
        }
        ConfigurationBuilder configurationBuilder = configurationBuilderHolder.getConfigurationBuilder(vendorName);
        ConfigurationFile configurationFile = configurationBuilder.buildConfig(device);
        if (!ProvisioningMode.HTTP.equals(device.getProvisioningMode())) {
            configurationWriter.writeConfig(configurationFile);
        }
        return configurationFile;
    }
}
