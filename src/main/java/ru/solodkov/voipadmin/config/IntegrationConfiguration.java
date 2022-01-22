package ru.solodkov.voipadmin.config;

import ch.loway.oss.ari4java.ARI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import ru.solodkov.voipadmin.integration.asterisk.service.AsteriskIntegrationService;

@Configuration
public class IntegrationConfiguration {

    @ConditionalOnExpression("'${integration.asterisk.enable}'.equals('true')")
    public AsteriskIntegrationService asteriskIntegrationService(ARI ari) {
        return new AsteriskIntegrationService();
    }
}
