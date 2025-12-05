package com.camunda_integration.demo.api.controller;

public class MaterialCalculateRequest {

    private String manufacturingMachineId;
    private int requiredUnits;

    public String getManufacturingMachineId() {
        return manufacturingMachineId;
    }

    public void setManufacturingMachineId(String manufacturingMachineId) {
        this.manufacturingMachineId = manufacturingMachineId;
    }

    public int getRequiredUnits() {
        return requiredUnits;
    }

    public void setRequiredUnits(int requiredUnits) {
        this.requiredUnits = requiredUnits;
    }
}
