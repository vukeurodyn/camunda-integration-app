package com.camunda_integration.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialTotalDto {

    @JsonProperty("materialName")
    private String materialName;

    @JsonProperty("totalWeight")
    private BigDecimal totalWeight;

    @JsonProperty("unitCode")
    private String unitCode;

    @JsonProperty("components")
    private List<MaterialComponentShareDto> components;

    public String getMaterialName() {
        return materialName;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public List<MaterialComponentShareDto> getComponents() {
        return components;
    }
}
