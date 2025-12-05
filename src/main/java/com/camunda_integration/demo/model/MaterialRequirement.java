package com.camunda_integration.demo.model;

import java.math.BigDecimal;

public class MaterialRequirement {

    private String materialId;
    private String materialName;

    private BigDecimal perUnitWeight;
    private BigDecimal requiredWeight;
    private BigDecimal stockWeight;
    private BigDecimal missingWeight;

    private int maxProducibleUnits;
    private boolean sufficient;

    public MaterialRequirement(
            String materialId,
            String materialName,
            BigDecimal perUnitWeight,
            BigDecimal requiredWeight,
            BigDecimal stockWeight,
            BigDecimal missingWeight,
            int maxProducibleUnits,
            boolean sufficient
    ) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.perUnitWeight = perUnitWeight;
        this.requiredWeight = requiredWeight;
        this.stockWeight = stockWeight;
        this.missingWeight = missingWeight;
        this.maxProducibleUnits = maxProducibleUnits;
        this.sufficient = sufficient;
    }

    public String getMaterialId() { return materialId; }

    public String getMaterialName() { return materialName; }

    public BigDecimal getPerUnitWeight() { return perUnitWeight; }

    public BigDecimal getRequiredWeight() { return requiredWeight; }

    public BigDecimal getStockWeight() { return stockWeight; }

    public BigDecimal getMissingWeight() { return missingWeight; }

    public int getMaxProducibleUnits() { return maxProducibleUnits; }

    public boolean isSufficient() { return sufficient; }


    public void setStockWeight(BigDecimal stockWeight) {
        this.stockWeight = stockWeight;
    }

    public void setMissingWeight(BigDecimal missingWeight) {
        this.missingWeight = missingWeight;
    }

    public void setMaxProducibleUnits(int maxProducibleUnits) {
        this.maxProducibleUnits = maxProducibleUnits;
    }

    public void setSufficient(boolean sufficient) {
        this.sufficient = sufficient;
    }
}
