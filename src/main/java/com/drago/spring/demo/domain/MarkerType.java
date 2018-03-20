package com.drago.spring.demo.domain;


public enum MarkerType {
    TYPE1("Marker form 1"), TYPE2("Marker form 2"), TYPE3("Marker form 3");

    private String name;

    MarkerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
