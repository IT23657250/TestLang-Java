package com.testlang.ast;

import java.util.List;

/**
 * Represents a test block
 */
public class TestBlock {
    private String name;
    private List<Statement> statements;

    public TestBlock(String name, List<Statement> statements) {
        this.name = name;
        this.statements = statements;
    }

    public String getName() {
        return name;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "TestBlock{name='" + name + "', statements=" + statements + '}';
    }
}
