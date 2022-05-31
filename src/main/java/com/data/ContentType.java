package com.data;

public enum ContentType {
    CONTENT_TYPE("image/png");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
