package com.camunda_integration.demo.vars;

import java.util.Map;

public final class CamundaVars {
    public record VariableValue(Object value, String type) {}

    public static Map<String, VariableValue> of(Map<String, Object> kv) {
        return kv.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new VariableValue(e.getValue(), toType(e.getValue()))
                ));
    }

    private static String toType(Object v) {
        if (v == null) return "String";
        return switch (v) {
            case String s -> "String";
            case Integer i -> "Integer";
            case Long l -> "Long";
            case Double d -> "Double";
            case Boolean b -> "Boolean";
            default -> "String"; // keep it simple for now
        };
    }

    public record StartProcessRequest(
            String businessKey,
            Map<String, VariableValue> variables
    ) {}

    public record StartProcessResponse(String id) {}
}
