package com.camunda_integration.demo.api.controller;

import com.camunda_integration.demo.model.BatchCalculationResult;
import com.camunda_integration.demo.model.dto.ComponentDto;
import com.camunda_integration.demo.model.dto.ManufacturingMachineDto;
import com.camunda_integration.demo.service.MaterialCalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    private final ObjectMapper mapper;
    private final MaterialCalculationService service;
    private final RestTemplate restTemplate;

    private static final String ORION_BASE_URL =
            "http://host.docker.internal:1026/ngsi-ld/v1/entities/";

    public MaterialController(ObjectMapper mapper, MaterialCalculationService service) {
        this.mapper = mapper;
        this.service = service;
        this.restTemplate = new RestTemplate();
    }

    // -----------------------------------------------------------------------------
    // POST /api/material/calculate   → Camunda Task 1
    // -----------------------------------------------------------------------------
    @PostMapping("/calculate")
    public Map<String, Object> calculate(@RequestBody MaterialCalculateRequest req) throws Exception {

        String machineId = req.getManufacturingMachineId();
        int quantity = req.getRequiredUnits();

        // Fetch the machine entity
        HttpHeaders headers = new HttpHeaders();
        headers.add("NGSILD-Tenant", "circuloos_demo");

        ResponseEntity<String> response = restTemplate.exchange(
                ORION_BASE_URL + machineId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        ManufacturingMachineDto machine =
                mapper.readValue(response.getBody(), ManufacturingMachineDto.class);

        // Store calculation in cache
        String calcId = service.storeInitialCalculation(machine, quantity);

        return Map.of(
                "calculationId", calcId,
                "machineId", machine.getId(),
                "requestedUnits", quantity
        );
    }

    // -----------------------------------------------------------------------------
    // GET /api/material/validate?calcId=...   → Camunda Task 2
    // -----------------------------------------------------------------------------
    @GetMapping("/validate")
    public StockResponse validate(@RequestParam("calcId") String calcId) throws Exception {

        BatchCalculationResult result = service.getResult(calcId);
        if (result == null) {
            return new StockResponse(false, 0);
        }

        int maxProducible = Integer.MAX_VALUE;

        // For each component: count how many full trays possible
        for (String compId : result.getComponentIds()) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("NGSILD-Tenant", "circuloos_demo");

            ResponseEntity<String> response = restTemplate.exchange(
                    ORION_BASE_URL + compId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            ComponentDto comp = mapper.readValue(response.getBody(), ComponentDto.class);

            int availableUnits = comp.getStockLevel().intValue();

            // Each tray requires exactly ONE of each component
            maxProducible = Math.min(maxProducible, availableUnits);
        }

        boolean sufficient = maxProducible >= result.getRequestedQuantity();

        return new StockResponse(sufficient, maxProducible);
    }
}
