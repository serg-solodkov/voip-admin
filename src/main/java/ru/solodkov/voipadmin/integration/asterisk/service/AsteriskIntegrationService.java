package ru.solodkov.voipadmin.integration.asterisk.service;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.Endpoint;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.solodkov.voipadmin.service.dto.AsteriskMonitoringSummaryDTO;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;


@Service
public class AsteriskIntegrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsteriskIntegrationService.class);

    @Value("${integration.asterisk.enable:false}")
    private boolean enable;

    @Value("${integration.asterisk.host:127.0.0.1}")
    private String host;

    @Value("${integration.asterisk.login:admin}")
    private String login;

    @Value("${integration.asterisk.password:admin}")
    private String password;

    private ARI ari;

    @PostConstruct
    public void init() {
        getConnection();
    }

    public void getConnection() {
        LOGGER.debug("enable = " + enable);
        if (this.enable) {
            try {
                this.ari = ARI.build(host, "VoIP-Admin", login, password, AriVersion.IM_FEELING_LUCKY);
                LOGGER.debug("host = " + host);
            } catch (ARIException ex) {
                LOGGER.error("Unable to connect to asterisk on host = " + host);
            }
        }
    }

    public AsteriskMonitoringSummaryDTO getSummary() {
        AsteriskMonitoringSummaryDTO dto = new AsteriskMonitoringSummaryDTO();
        dto.setEndpoints(getPeers());
        return dto;
    }

    public List<Endpoint> getPeers() {
        try {
            return this.ari.endpoints().list().execute();
        } catch (RestException ex) {
            return Collections.emptyList();
        }
    }

    public Endpoint getPeer() {
        try {
            Endpoint endpoint = this.ari.endpoints().get().execute();
            return endpoint;
        } catch (RestException ex) {
            return null;
        }
    }
}
