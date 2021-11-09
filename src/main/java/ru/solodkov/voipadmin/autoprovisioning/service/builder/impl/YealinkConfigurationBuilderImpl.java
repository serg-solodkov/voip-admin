package ru.solodkov.voipadmin.autoprovisioning.service.builder.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.ConfigurationBuilder;
import ru.solodkov.voipadmin.autoprovisioning.service.builder.util.PlainTextConfigAttrBuilder;
import ru.solodkov.voipadmin.domain.Device;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service("yealink")
public class YealinkConfigurationBuilderImpl implements ConfigurationBuilder {

    private final static String CONFIG_FILE_EXTENSION = "cfg";

    private static final String SECTION_TITLE_TEMPLATE = "##{0}{1}{2}##";
    private static final int SECTION_TITLE_LINE_LENGTH = 83;

    @Override
    public ConfigurationFile buildConfig(Device device) {
        if (isNull(device.getModel().getConfigTemplate())) {
            return null;
        }
        return buildConfigWithTemplate(device);
    }

    private ConfigurationFile buildConfigWithTemplate(Device device) {
        ConfigurationFile configurationFile = new ConfigurationFile();
        configurationFile.setFileName(MessageFormat.format("{0}.{1}", device.getMac(), CONFIG_FILE_EXTENSION));

        byte[] configTemplateFile = device.getModel().getConfigTemplate();
        String configTemplate = new String(configTemplateFile, StandardCharsets.UTF_8);

        PlainTextConfigAttrBuilder attrBuilder = new PlainTextConfigAttrBuilder(configTemplate);
        // Main settings
        attrBuilder
            .set("static.security.user_name.var", device.getWebLogin())
            .set("static.security.user_password", device.getWebPassword())
            .set("static.network.internet_port.type", device.getDhcpEnabled() ? "0" : "2")
            .set("local_time.ntp_server1", device.getNtpServer());

        if (!device.getDhcpEnabled()) {
            attrBuilder
                .set("static.network.internet_port.ip", device.getIpAddress())
                .set("static.network.internet_port.mask", device.getSubnetMask())
                .set("static.network.internet_port.gateway", device.getDefaultGw())
                .set("static.network.primary_dns", device.getDns1())
                .set("static.network.secondary_dns", device.getDns2());
        }

        if (nonNull(device.getProvisioningUrl())) {
            String provisioningUrl = MessageFormat.format(
                "{0}://{1}",
                device.getProvisioningMode().toString().toLowerCase(),
                device.getProvisioningUrl()
            );
            attrBuilder
                .set("static.auto_provision.server.url", provisioningUrl);
        }

        attrBuilder.addNewLine();
        attrBuilder.addNewLine();

        addSectionTitle(attrBuilder, "Below sections were inserted using VoIP-Admin");
        attrBuilder.addNewLine();

        // Line settings
        if (nonNull(device.getVoipAccounts()) && !device.getVoipAccounts().isEmpty()) {
            device.getVoipAccounts().forEach(voipAccount -> {
                attrBuilder.remove("account." + voipAccount.getLineNumber());
                attrBuilder.addNewLine();
                addSectionTitle(attrBuilder, "Account " + voipAccount.getLineNumber() + " settings");
                attrBuilder.addNewLine();

                attrBuilder.add("account." + voipAccount.getLineNumber() + ".enable",voipAccount.getLineEnable() ? "1" : "0");
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".label", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".display_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".auth_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".user_name", voipAccount.getUsername());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".password", voipAccount.getPassword());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".sip_server.1.address", voipAccount.getSipServer());
                attrBuilder.add("account." + voipAccount.getLineNumber() + ".sip_server.1.port", voipAccount.getSipPort());

            });
        }

        // Additional settings
        if (nonNull(device.getSettings()) && !device.getSettings().isEmpty()) {
            device.getSettings().forEach(setting -> {
                Object value = setting.getOption().getValueType().equals(OptionValueType.SELECT) ?
                    setting.getSelectedValues().iterator().next().getValue() : setting.getTextValue();
                attrBuilder.set(setting.getOption().getCode(), value);
            });
        }

        configurationFile.setContent(attrBuilder.toString().getBytes(StandardCharsets.UTF_8));
        return configurationFile;
    }

    private void addSectionTitle(PlainTextConfigAttrBuilder attrBuilder, String sectionTitle) {
        attrBuilder
            .addComment(StringUtils.repeat("#", SECTION_TITLE_LINE_LENGTH))
            .addComment(
                MessageFormat.format(
                    SECTION_TITLE_TEMPLATE,
                    StringUtils.repeat(" ", (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 2),
                    sectionTitle,
                    StringUtils.repeat(" ", sectionTitle.length() % 2 != 0 ?
                        (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 2 : (SECTION_TITLE_LINE_LENGTH - sectionTitle.length())/2 - 1)
                )
            )
            .addComment(StringUtils.repeat("#", SECTION_TITLE_LINE_LENGTH));
    }
}
