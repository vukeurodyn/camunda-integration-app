package com.camunda_integration.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialComponentShareDto {

    @JsonProperty("componentId")
    private String componentId;

    @JsonProperty("componentName")
    private String componentName;

    @JsonProperty("weight")
    private BigDecimal weight;

    public String getComponentId() {
        return componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public BigDecimal getWeight() {
        return weight;
    }
}
