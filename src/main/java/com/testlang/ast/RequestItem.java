package com.testlang.ast;

/**
 * Represents a request item (header or body)
 */
public class RequestItem {
    private String type; // "header" or "body"
    private String key;   // For headers
    private String value;

    // For body
    public RequestItem(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // For header
    public RequestItem(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if ("body".equals(type)) {
            return "RequestItem{type='" + type + "', value='" + value + "'}";
        } else {
            return "RequestItem{type='" + type + "', key='" + key + "', value='" + value + "'}";
        }
    }
}
