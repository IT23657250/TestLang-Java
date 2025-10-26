package com.testlang.ast;

/**
 * Represents a single config item (base_url or header)
 */
public class ConfigItem {
    private String type; // "base_url" or "header"
    private String key;
    private String value;

    // For base_url
    public ConfigItem(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // For header
    public ConfigItem(String type, String key, String value) {
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
        if ("base_url".equals(type)) {
            return "ConfigItem{type='" + type + "', value='" + value + "'}";
        } else {
            return "ConfigItem{type='" + type + "', key='" + key + "', value='" + value + "'}";
        }
    }
}
