package com.order.process.system.demo.model;

/**
 * Enum for the different status' an order can be in.
 * contains a next method in order to determine the
 * next status for an object.
 * next() will return null if the order has been completed.
 */

public enum Status {
    PENDING(0, "PENDING"),
    PAID(1, "PAID"),
    COMPLETED(2, "COMPLETED");

    private final String label;
    private final int code;

    Status(int code, String label) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public int getCode() {
        return code;
    }

    public Status next() {
        if (values().length > ordinal() + 1) {
            return values()[ordinal() + 1];
        }else{
            return null;
        }
    }
}