package com.testlang.ast;

import java.util.List;

/**
 * Represents the config block with base_url and default headers
 */
public class ConfigBlock {
    private List<ConfigItem> items;

    public ConfigBlock(List<ConfigItem> items) {
        this.items = items;
    }

    public List<ConfigItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "ConfigBlock{" + "items=" + items + '}';
    }
}
