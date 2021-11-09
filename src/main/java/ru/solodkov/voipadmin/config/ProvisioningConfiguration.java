package ru.solodkov.voipadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.ConfigurationWriter;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.impl.LocalStorageConfigurationWriterImpl;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.impl.NoOpConfigurationWriterImpl;
import ru.solodkov.voipadmin.autoprovisioning.service.writer.impl.RemoteStorageConfigurationWriterImpl;

@Configuration
public class ProvisioningConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvisioningConfiguration.class);

    @Bean
    @ConditionalOnProperty({"provisioning.ftp.enable", "provisioning.ftp.host"})
    public ConfigurationWriter ftpRemoteConfigurationWriter() {
        return new RemoteStorageConfigurationWriterImpl();
    }

    @Bean
    @ConditionalOnProperty({"provisioning.tftp.enable", "provisioning.tftp.host"})
    public ConfigurationWriter tftpRemoteConfigurationWriter() {
        return new RemoteStorageConfigurationWriterImpl();
    }

    @Bean
    @ConditionalOnExpression("'${provisioning.ftp.enable}'.equals('true') "
        + "and T(org.springframework.util.StringUtils).isEmpty('${provisioning.ftp.host:}')")
    public ConfigurationWriter ftpLocalConfigurationWriter() {
        return new LocalStorageConfigurationWriterImpl();
    }

    @Bean
    @ConditionalOnExpression("'${provisioning.tftp.enable}'.equals('true') "
        + "and T(org.springframework.util.StringUtils).isEmpty('${provisioning.tftp.host:}')")
    public ConfigurationWriter tftpLocalConfigurationWriter() {
        return new LocalStorageConfigurationWriterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigurationWriter noOpConfigurationWriter() {
        return new NoOpConfigurationWriterImpl();
    }
}
