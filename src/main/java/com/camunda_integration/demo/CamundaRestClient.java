package com.camunda_integration.demo;

import com.camunda_integration.demo.vars.CamundaVars;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CamundaRestClient {

    private final WebClient http;
    private final String baseUrl;

    public CamundaRestClient(@Value("${camunda.rest.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.http = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<String> startByKey(
            String processKey,
            String businessKey,
            Map<String, Object> simpleVars
    ) {
        var body = new CamundaVars.StartProcessRequest(
                businessKey,
                CamundaVars.of(simpleVars)
        );

        return http.post()
                .uri("/process-definition/key/{key}/start", processKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(CamundaVars.StartProcessResponse.class)
                .map(CamundaVars.StartProcessResponse::id);
    }
}
