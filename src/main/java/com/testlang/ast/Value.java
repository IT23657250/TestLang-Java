package com.testlang.ast;

/**
 * Represents a value (string or integer)
 */
public class Value {
    private String stringValue;
    private Integer intValue;
    private boolean isString;

    public Value(String value) {
        this.stringValue = value;
        this.isString = true;
    }

    public Value(Integer value) {
        this.intValue = value;
        this.isString = false;
    }

    public boolean isString() {
        return isString;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    @Override
    public String toString() {
        if (isString) {
            return "Value{string='" + stringValue + "'}";
        } else {
            return "Value{int=" + intValue + '}';
        }
    }
}
