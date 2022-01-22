package ru.solodkov.voipadmin.integration.asterisk.controller;

import ch.loway.oss.ari4java.generated.models.Endpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.solodkov.voipadmin.integration.asterisk.service.AsteriskIntegrationService;

import java.util.List;

@RestController
@RequestMapping("/asterisk")
public class AsteriskIntegrationController {

    private final AsteriskIntegrationService asteriskIntegrationService;

    public AsteriskIntegrationController(AsteriskIntegrationService asteriskIntegrationService) {
        this.asteriskIntegrationService = asteriskIntegrationService;
    }

    @GetMapping("/peers")
    public List<Endpoint> getEndpoints() {
        return asteriskIntegrationService.getPeers();
    }
}
