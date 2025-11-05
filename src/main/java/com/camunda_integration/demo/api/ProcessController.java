package com.camunda_integration.demo.api;

import com.camunda_integration.demo.ProcessStarterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final ProcessStarterService service;

    public ProcessController(ProcessStarterService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public Mono<ResponseEntity<?>> start(@Valid @RequestBody StartRequest req) {
        return service.start(req)
                .map(pi -> ResponseEntity.ok().body(new StartResponse(pi)));
    }

    public record StartResponse(String processInstanceId) {}
}
