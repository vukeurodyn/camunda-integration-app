package com.camunda_integration.demo.model;

import java.math.BigDecimal;
import java.util.List;

public class BatchCalculationResult {

    private final String calculationId;
    private final String machineId;
    private final int requestedQuantity;

    private final BigDecimal perUnitWeight;
    private final BigDecimal totalRequiredWeight;

    private final int maxProducibleUnits;
    private final boolean sufficient;

    private final List<MaterialRequirement> materials;

    private final List<String> componentIds;

    public BatchCalculationResult(
            String calculationId,
            String machineId,
            int requestedQuantity,
            BigDecimal perUnitWeight,
            BigDecimal totalRequiredWeight,
            int maxProducibleUnits,
            boolean sufficient,
            List<MaterialRequirement> materials,
            List<String> componentIds   // ← add here
    ) {
        this.calculationId = calculationId;
        this.machineId = machineId;
        this.requestedQuantity = requestedQuantity;
        this.perUnitWeight = perUnitWeight;
        this.totalRequiredWeight = totalRequiredWeight;
        this.maxProducibleUnits = maxProducibleUnits;
        this.sufficient = sufficient;
        this.materials = materials;
        this.componentIds = componentIds;  // ← and assign here
    }

    public String getCalculationId() {
        return calculationId;
    }

    public String getMachineId() {
        return machineId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public BigDecimal getPerUnitWeight() {
        return perUnitWeight;
    }

    public BigDecimal getTotalRequiredWeight() {
        return totalRequiredWeight;
    }

    public int getMaxProducibleUnits() {
        return maxProducibleUnits;
    }

    public boolean isSufficient() {
        return sufficient;
    }

    public List<MaterialRequirement> getMaterials() {
        return materials;
    }

    public List<String> getComponentIds() {
        return componentIds;
    }
}
