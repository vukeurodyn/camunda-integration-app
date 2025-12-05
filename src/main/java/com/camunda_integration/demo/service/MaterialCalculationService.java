package com.camunda_integration.demo.service;

import com.camunda_integration.demo.model.BatchCalculationResult;
import com.camunda_integration.demo.model.MaterialRequirement;
import com.camunda_integration.demo.model.dto.ManufacturingMachineDto;
import com.camunda_integration.demo.model.dto.MaterialTotalDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MaterialCalculationService {

    private final Map<String, BatchCalculationResult> cache = new HashMap<>();

    public String storeInitialCalculation(ManufacturingMachineDto machine, int quantity) {

        String calcId = UUID.randomUUID().toString();

        BatchCalculationResult result = calculateRequirementsOnly(machine, quantity);

        cache.put(calcId, result);

        return calcId;
    }

    public BatchCalculationResult getResult(String calcId) {
        return cache.get(calcId);
    }

    private BatchCalculationResult calculateRequirementsOnly(
            ManufacturingMachineDto machine,
            int quantity
    ) {
        BigDecimal perUnitWeight = machine.getTotalWeight() != null
                ? machine.getTotalWeight().getValue()
                : BigDecimal.ZERO;

        BigDecimal totalRequiredWeight = perUnitWeight.multiply(BigDecimal.valueOf(quantity));

        List<MaterialRequirement> reqs = new ArrayList<>();

        for (var entry : machine.getMaterialTotals().entrySet()) {

            String materialId = entry.getKey();
            MaterialTotalDto dto = entry.getValue();

            BigDecimal perUnitMaterial = dto.getTotalWeight();
            BigDecimal required = perUnitMaterial.multiply(BigDecimal.valueOf(quantity));

            reqs.add(new MaterialRequirement(
                    materialId,
                    dto.getMaterialName(),
                    perUnitMaterial,
                    required,
                    BigDecimal.ZERO,   // initial stock unknown
                    BigDecimal.ZERO,   // initial missing unknown
                    0,                 // initial max unknown
                    false              // initial sufficient unknown
            ));
        }

        List<String> componentIds = machine.getHasComponentIds();

        return new BatchCalculationResult(
                UUID.randomUUID().toString(),
                machine.getId(),
                quantity,
                perUnitWeight,
                totalRequiredWeight,
                0,
                false,
                reqs,
                componentIds
        );
    }
}
