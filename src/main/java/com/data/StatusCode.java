package com.data;

/**
 * Enum for the valid values of status code received from request to API
 */
public enum StatusCode {
    SUCCESS(200);

    private final int value;

    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
