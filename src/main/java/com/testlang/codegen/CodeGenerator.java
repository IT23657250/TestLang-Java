package com.testlang.codegen;

import java.util.HashMap;
import java.util.Map;

import com.testlang.ast.Assertion;
import com.testlang.ast.ConfigItem;
import com.testlang.ast.Program;
import com.testlang.ast.Request;
import com.testlang.ast.RequestItem;
import com.testlang.ast.Statement;
import com.testlang.ast.TestBlock;
import com.testlang.ast.Variable;

/**
 * Generates JUnit 5 test code from the AST
 */
public class CodeGenerator {
    private Program program;
    private Map<String, String> variables;
    private StringBuilder code;
    private int indentLevel;

    public CodeGenerator(Program program) {
        this.program = program;
        this.variables = new HashMap<>();
        this.code = new StringBuilder();
        this.indentLevel = 0;
    }

    public String generate() {
        // Process variables
        for (Variable var : program.getVariables()) {
            if (var.getValue().isString()) {
                variables.put(var.getName(), var.getValue().getStringValue());
            } else {
                variables.put(var.getName(), String.valueOf(var.getValue().getIntValue()));
            }
        }

        // Generate class header
        generateImports();
        generateClassHeader();
        generateConfigFields();
        generateSetupMethod();
        
        // Generate test methods
        for (TestBlock test : program.getTests()) {
            generateTestMethod(test);
        }

        // Close class
        writeLine("}");

        return code.toString();
    }

    private void generateImports() {
        writeLine("import org.junit.jupiter.api.*;");
        writeLine("import static org.junit.jupiter.api.Assertions.*;");
        writeLine("import java.net.http.*;");
        writeLine("import java.net.*;");
        writeLine("import java.time.Duration;");
        writeLine("import java.nio.charset.StandardCharsets;");
        writeLine("import java.util.*;");
        writeLine();
    }

    private void generateClassHeader() {
        writeLine("public class GeneratedTests {");
        indentLevel++;
    }

    private void generateConfigFields() {
        // Base URL
        String baseUrl = "http://localhost:8080";
        if (program.getConfig() != null) {
            for (ConfigItem item : program.getConfig().getItems()) {
                if ("base_url".equals(item.getType())) {
                    baseUrl = item.getValue();
                    break;
                }
            }
        }
        writeLine("static String BASE = \"" + baseUrl + "\";");
        writeLine("static Map<String, String> DEFAULT_HEADERS = new HashMap<>();");
        writeLine("static HttpClient client;");
        writeLine();
    }

    private void generateSetupMethod() {
        writeLine("@BeforeAll");
        writeLine("static void setup() {");
        indentLevel++;
        
        writeLine("client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();");
        
        // Add default headers from config
        if (program.getConfig() != null) {
            for (ConfigItem item : program.getConfig().getItems()) {
                if ("header".equals(item.getType())) {
                    writeLine("DEFAULT_HEADERS.put(\"" + item.getKey() + "\", \"" + item.getValue() + "\");");
                }
            }
        }
        
        indentLevel--;
        writeLine("}");
        writeLine();
    }

    private void generateTestMethod(TestBlock test) {
        writeLine("@Test");
        writeLine("void test_" + test.getName() + "() throws Exception {");
        indentLevel++;

        // Process statements
        boolean hasRequest = false;
        for (Statement stmt : test.getStatements()) {
            if (stmt instanceof Request) {
                if (hasRequest) {
                    // Close previous request's response handling
                    writeLine();
                }
                generateRequest((Request) stmt);
                hasRequest = true;
            } else if (stmt instanceof Assertion) {
                if (!hasRequest) {
                    throw new RuntimeException("Assertion before request in test " + test.getName());
                }
                generateAssertion((Assertion) stmt);
            }
        }

        indentLevel--;
        writeLine("}");
        writeLine();
    }

    private void generateRequest(Request request) {
        String path = substituteVariables(request.getPath());
        String url;
        
        // Determine if we need to prepend base URL
        if (path.startsWith("http://") || path.startsWith("https://")) {
            url = path;
        } else {
            url = "BASE + \"" + path + "\"";
        }

        // Build request
        writeLine("HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(" + url + "))");
        indentLevel++;
        writeLine(".timeout(Duration.ofSeconds(10))");

        // Determine request body
        String bodyContent = null;
        for (RequestItem item : request.getItems()) {
            if ("body".equals(item.getType())) {
                bodyContent = substituteVariables(item.getValue());
                break;
            }
        }

        // Set HTTP method
        switch (request.getMethod()) {
            case "GET":
                writeLine(".GET();");
                break;
            case "DELETE":
                writeLine(".DELETE();");
                break;
            case "POST":
                if (bodyContent != null) {
                    writeLine(".POST(HttpRequest.BodyPublishers.ofString(\"" + escapeJava(bodyContent) + "\"));");
                } else {
                    writeLine(".POST(HttpRequest.BodyPublishers.noBody());");
                }
                break;
            case "PUT":
                if (bodyContent != null) {
                    writeLine(".PUT(HttpRequest.BodyPublishers.ofString(\"" + escapeJava(bodyContent) + "\"));");
                } else {
                    writeLine(".PUT(HttpRequest.BodyPublishers.noBody());");
                }
                break;
        }
        indentLevel--;

        // Add request-specific headers
        for (RequestItem item : request.getItems()) {
            if ("header".equals(item.getType())) {
                writeLine("b.header(\"" + item.getKey() + "\", \"" + item.getValue() + "\");");
            }
        }

        // Add default headers
        writeLine("for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());");

        // Send request
        writeLine("HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));");
        writeLine();
    }

    private void generateAssertion(Assertion assertion) {
        switch (assertion.getType()) {
            case "status_equals":
                int statusCode = (Integer) assertion.getValue1();
                writeLine("assertEquals(" + statusCode + ", resp.statusCode());");
                break;

            case "header_equals":
                String headerKey = (String) assertion.getValue1();
                String headerValue = (String) assertion.getValue2();
                writeLine("assertEquals(\"" + escapeJava(headerValue) + "\", " +
                         "resp.headers().firstValue(\"" + headerKey + "\").orElse(\"\"));");
                break;

            case "header_contains":
                headerKey = (String) assertion.getValue1();
                String headerSubstr = (String) assertion.getValue2();
                writeLine("assertTrue(resp.headers().firstValue(\"" + headerKey + "\").orElse(\"\").contains(\"" + 
                         escapeJava(headerSubstr) + "\"));");
                break;

            case "body_contains":
                String bodySubstr = (String) assertion.getValue1();
                writeLine("assertTrue(resp.body().contains(\"" + escapeJava(bodySubstr) + "\"));");
                break;

            case "status_range":
                int start = (Integer) assertion.getValue1();
                int end = (Integer) assertion.getValue2();
                writeLine("assertTrue(resp.statusCode() >= " + start + " && resp.statusCode() <= " + end + ", " +
                         "\"Status code should be in range [" + start + ".." + end + "]\");");
                break;
        }
    }

    private String substituteVariables(String text) {
        String result = text;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("$" + entry.getKey(), entry.getValue());
        }
        return result;
    }

    private String escapeJava(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private void writeLine(String line) {
        if (line.isEmpty()) {
            code.append("\n");
        } else {
            for (int i = 0; i < indentLevel; i++) {
                code.append("    ");
            }
            code.append(line).append("\n");
        }
    }

    private void writeLine() {
        code.append("\n");
    }
}
