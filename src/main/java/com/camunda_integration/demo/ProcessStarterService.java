package com.camunda_integration.demo;

import com.camunda_integration.demo.api.StartRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessStarterService {

    private final CamundaRestClient camunda;
    private final String processKey;

    public ProcessStarterService(CamundaRestClient camunda,
                                 @Value("${process.key}") String processKey) {
        this.camunda = camunda;
        this.processKey = processKey;
    }

    public Mono<String> start(StartRequest req) {
        Map<String, Object> vars = new HashMap<>();
        // 1) Keycloak inputs (used by your HTTP connector in BPMN)
        vars.put("kc_token_url", req.kcTokenUrl());
        vars.put("kc_client_id", req.kcClientId());
        vars.put("kc_client_secret", req.kcClientSecret());

        // 2) Circuloos/Orion inputs (used by your HTTP connector in BPMN)
        vars.put("orion_base_url", req.orionBaseUrl());
        vars.put("applicant_email", req.applicantEmail());

        // 3) Who to email for review (we’ll use this later when we add the email sender)
        vars.put("reviewEmail", req.reviewEmail());

        return camunda.startByKey(processKey, req.businessKey(), vars);
    }
}
