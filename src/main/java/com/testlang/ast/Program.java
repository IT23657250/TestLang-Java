package com.testlang.ast;

import java.util.List;

/**
 * Root node representing the entire test program
 */
public class Program {
    private ConfigBlock config;
    private List<Variable> variables;
    private List<TestBlock> tests;

    public Program(ConfigBlock config, List<Variable> variables, List<TestBlock> tests) {
        this.config = config;
        this.variables = variables;
        this.tests = tests;
    }

    public ConfigBlock getConfig() {
        return config;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<TestBlock> getTests() {
        return tests;
    }

    @Override
    public String toString() {
        return "Program{" +
                "config=" + config +
                ", variables=" + variables +
                ", tests=" + tests +
                '}';
    }
}
