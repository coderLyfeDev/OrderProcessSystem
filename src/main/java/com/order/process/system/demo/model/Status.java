package com.order.process.system.demo.model;

public enum Status {
    PENDING("PENDING"),
    PAID("PAID"),
    COMPLETED("COMPLETED");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}