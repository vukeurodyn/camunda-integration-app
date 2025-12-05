package com.camunda_integration.demo.model.dto;

import com.camunda_integration.demo.model.NgsiProperty;
import com.camunda_integration.demo.model.NgsiPropertyWithUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManufacturingMachineDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private NgsiProperty<String> name;

    @JsonProperty("description")
    private NgsiProperty<String> description;

    @JsonProperty("totalWeight")
    private NgsiPropertyWithUnit<BigDecimal> totalWeight;

    @JsonProperty("hasComponent")
    private HasComponentRelation hasComponent;

    @JsonProperty("materialTotals")
    private MaterialTotalsWrapper materialTotalsWrapper;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public NgsiProperty<String> getName() {
        return name;
    }

    public NgsiProperty<String> getDescription() {
        return description;
    }

    public NgsiPropertyWithUnit<BigDecimal> getTotalWeight() {
        return totalWeight;
    }

    public Map<String, MaterialTotalDto> getMaterialTotals() {
        return materialTotalsWrapper != null ? materialTotalsWrapper.value : Map.of();
    }

    public List<String> getHasComponentIds() {
        if (hasComponent == null || hasComponent.object == null) {
            return Collections.emptyList();
        }
        return hasComponent.object;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HasComponentRelation {
        @JsonProperty("object")
        public List<String> object;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MaterialTotalsWrapper {
        @JsonProperty("value")
        public Map<String, MaterialTotalDto> value;
    }
}
