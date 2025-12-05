package com.camunda_integration.demo.model;

public class NgsiProperty<T> {
    private String type;
    private T value;

    public String getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}

