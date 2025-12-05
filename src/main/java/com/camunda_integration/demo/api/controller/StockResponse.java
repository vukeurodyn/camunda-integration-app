package com.camunda_integration.demo.api.controller;


public class StockResponse {
    private final boolean sufficient;
    private final int maxProducibleUnits;

    public StockResponse(boolean sufficient, int maxProducibleUnits) {
        this.sufficient = sufficient;
        this.maxProducibleUnits = maxProducibleUnits;
    }

    public boolean isSufficient() {
        return sufficient;
    }

    public int getMaxProducibleUnits() {
        return maxProducibleUnits;
    }
}


