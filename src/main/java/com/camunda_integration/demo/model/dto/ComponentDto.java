package com.camunda_integration.demo.model.dto;

import com.camunda_integration.demo.model.NgsiProperty;
import com.camunda_integration.demo.model.NgsiPropertyWithUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private NgsiProperty<String> name;

    @JsonProperty("weight")
    private NgsiPropertyWithUnit<BigDecimal> weight; // value wrapper

    @JsonProperty("stockLevel")
    private NgsiProperty<BigDecimal> stockLevel;     // value wrapper

    @JsonProperty("hasMaterial")
    private Relationship hasMaterial;

    public String getId() {
        return id;
    }

    public BigDecimal getWeight() {
        return weight != null ? weight.getValue() : BigDecimal.ZERO;
    }

    public BigDecimal getStockLevel() {
        return stockLevel != null ? stockLevel.getValue() : BigDecimal.ZERO;
    }

    public String getMaterialId() {
        return hasMaterial != null ? hasMaterial.object : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Relationship {

        @JsonProperty("object")
        public String object;
    }
}
