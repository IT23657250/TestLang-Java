package com.testlang.ast;

/**
 * Represents an assertion
 */
public class Assertion extends Statement {
    private String type; // status_equals, header_equals, header_contains, body_contains, status_range
    private Object value1;
    private Object value2;
    private Object value3;

    // For status_equals
    public Assertion(String type, int statusCode) {
        this.type = type;
        this.value1 = statusCode;
    }

    // For header_equals, header_contains
    public Assertion(String type, String key, String value) {
        this.type = type;
        this.value1 = key;
        this.value2 = value;
    }

    // For body_contains
    public Assertion(String type, String value) {
        this.type = type;
        this.value1 = value;
    }

    // For status_range
    public Assertion(String type, int start, int end) {
        this.type = type;
        this.value1 = start;
        this.value2 = end;
    }

    public String getType() {
        return type;
    }

    public Object getValue1() {
        return value1;
    }

    public Object getValue2() {
        return value2;
    }

    public Object getValue3() {
        return value3;
    }

    @Override
    public String toString() {
        return "Assertion{type='" + type + "', value1=" + value1 + 
               ", value2=" + value2 + ", value3=" + value3 + '}';
    }
}
