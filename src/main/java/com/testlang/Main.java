package com.testlang;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.testlang.ast.Program;
import com.testlang.codegen.CodeGenerator;
import com.testlang.parser.Lexer;
import com.testlang.parser.parser;

/**
 * Main entry point for the TestLang++ compiler
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar testlang-java.jar <input.test> [output.java]");
            System.err.println("  <input.test>  : Path to the .test file to compile");
            System.err.println("  [output.java] : Optional output path (default: GeneratedTests.java)");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args.length > 1 ? args[1] : "GeneratedTests.java";

        try {
            System.out.println("TestLang++ Compiler");
            System.out.println("===================");
            System.out.println("Input:  " + inputFile);
            System.out.println("Output: " + outputFile);
            System.out.println();

            // Read input file
            String input = new String(Files.readAllBytes(Paths.get(inputFile)));

            // Create lexer and parser
            Lexer lexer = new Lexer(new StringReader(input));
            parser p = new parser(lexer);

            // Parse the input
            System.out.println("Parsing...");
            Program program = (Program) p.parse().value;
            System.out.println("✓ Parse successful");
            System.out.println();

            // Validate program
            validateProgram(program);
            System.out.println("✓ Validation successful");
            System.out.println();

            // Generate code
            System.out.println("Generating JUnit 5 code...");
            CodeGenerator generator = new CodeGenerator(program);
            String generatedCode = generator.generate();

            // Write output
            Files.write(Paths.get(outputFile), generatedCode.getBytes());
            System.out.println("✓ Code generation successful");
            System.out.println();
            System.out.println("Generated " + program.getTests().size() + " test(s)");
            System.out.println("Output written to: " + outputFile);

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + inputFile);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: I/O error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void validateProgram(Program program) throws Exception {
        // Check that we have at least one test
        if (program.getTests().isEmpty()) {
            throw new Exception("Validation error: Program must contain at least one test block");
        }

        // Validate each test
        for (var test : program.getTests()) {
            validateTest(test);
        }
    }

    private static void validateTest(com.testlang.ast.TestBlock test) throws Exception {
        int requestCount = 0;
        int assertionCount = 0;

        for (var stmt : test.getStatements()) {
            if (stmt instanceof com.testlang.ast.Request) {
                requestCount++;
            } else if (stmt instanceof com.testlang.ast.Assertion) {
                assertionCount++;
            }
        }

        if (requestCount == 0) {
            throw new Exception("Validation error: Test '" + test.getName() + 
                              "' must contain at least one request");
        }

        if (assertionCount < 2) {
            throw new Exception("Validation error: Test '" + test.getName() + 
                              "' must contain at least 2 assertions (found " + assertionCount + ")");
        }
    }
}
