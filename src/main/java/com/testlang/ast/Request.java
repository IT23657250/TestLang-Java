package com.testlang.ast;

import java.util.List;

/**
 * Represents an HTTP request
 */
public class Request extends Statement {
    private String method; // GET, POST, PUT, DELETE
    private String path;
    private List<RequestItem> items;

    public Request(String method, String path, List<RequestItem> items) {
        this.method = method;
        this.path = path;
        this.items = items;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public List<RequestItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Request{method='" + method + "', path='" + path + "', items=" + items + '}';
    }
}
