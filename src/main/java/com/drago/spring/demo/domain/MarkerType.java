package com.drago.spring.demo.domain;

public enum MarkerType {
    TYPE1("Marker form 1"), TYPE2("Marker form 2"), TYPE3("Marker form 3");

    private String displayName;

    MarkerType(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
