package ru.solodkov.voipadmin.autoprovisioning.service.builder;

import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.impl.CommonConfigurationBuilderImpl;

import java.util.Map;

@Service
public class ConfigurationBuilderHolder {

    private final Map<String, ConfigurationBuilder> configurationBuildersCollection;

    public ConfigurationBuilderHolder(Map<String, ConfigurationBuilder> configurationBuildersCollection) {
        this.configurationBuildersCollection = configurationBuildersCollection;
    }

    public ConfigurationBuilder getConfigurationBuilder(String vendor) {
        return configurationBuildersCollection.getOrDefault(vendor, new CommonConfigurationBuilderImpl());
    }
}
