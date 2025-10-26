# TestLang++ - HTTP API Testing DSL

[![Java Version](https://img.shields.io/badge/Java-11%2B-blue)](https://www.oracle.com/java/)
[![JUnit](https://img.shields.io/badge/JUnit-5.9.3-green)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A Domain-Specific Language (DSL) for describing HTTP API tests that compiles to runnable JUnit 5 tests using Java's built-in `HttpClient`.

## 🎯 Overview

TestLang++ allows you to write concise, readable HTTP API tests in a custom DSL syntax. The compiler parses `.test` files and generates complete JUnit 5 test classes with assertions, ready to run against your backend.

### Key Features

- ✅ **Simple DSL syntax** for GET, POST, PUT, DELETE requests
- ✅ **Variable substitution** for dynamic values
- ✅ **Configurable base URL** and default headers
- ✅ **Rich assertions** for status codes, headers, and body content
- ✅ **Generates idiomatic JUnit 5 code** using `java.net.http.HttpClient`
- ✅ **Meaningful error messages** for invalid syntax
- ✅ **Bonus features**: Status range assertions, triple-quoted strings

## 📚 Project Structure

```
TestLang-Java/
├── src/
│   ├── main/
│   │   ├── java/com/testlang/
│   │   │   ├── Main.java                    # Compiler entry point
│   │   │   ├── ast/                          # AST node classes
│   │   │   │   ├── Program.java
│   │   │   │   ├── ConfigBlock.java
│   │   │   │   ├── Variable.java
│   │   │   │   ├── TestBlock.java
│   │   │   │   ├── Request.java
│   │   │   │   ├── Assertion.java
│   │   │   │   └── ...
│   │   │   └── codegen/
│   │   │       └── CodeGenerator.java        # JUnit 5 code generator
│   │   ├── jflex/
│   │   │   └── lexer.flex                    # JFlex lexer specification
│   │   └── cup/
│   │       └── parser.cup                    # CUP parser specification
├── backend/                                   # Reference Spring Boot backend
│   ├── src/main/java/com/testlang/backend/
│   │   ├── TestLangBackendApplication.java
│   │   └── ApiController.java
│   └── pom.xml
├── example.test                               # Example test file
├── invalid.test                               # Invalid test for error demo
├── pom.xml                                    # Maven configuration
├── build.bat                                  # Build compiler
├── compile-test.bat                           # Compile .test to .java
├── run-tests.bat                              # Run generated tests
├── run-backend.bat                            # Start backend server
└── README.md                                  # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 11 or higher**
- **Maven 3.6+**
- **Windows** (scripts provided for Windows; adapt for Linux/Mac)

### Step 1: Build the Compiler

```batch
build.bat
```

This will:
1. Generate the lexer from `lexer.flex` using JFlex
2. Generate the parser from `parser.cup` using CUP
3. Compile all Java sources
4. Create an executable JAR with dependencies

### Step 2: Start the Backend Server

Open a **new terminal** and run:

```batch
run-backend.bat
```

Wait until you see:
```
Started TestLangBackendApplication in X.XXX seconds
```

The server will be available at `http://localhost:8080`

### Step 3: Compile a Test File

In your original terminal:

```batch
compile-test.bat example.test GeneratedTests.java
```

This reads `example.test` and generates `GeneratedTests.java`

### Step 4: Run the Generated Tests

```batch
run-tests.bat
```

You should see JUnit test results with all tests passing! ✅

## 📝 TestLang++ Language Reference

### File Structure

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

let variableName = "value";
let numericVar = 42;

test TestName {
  // HTTP requests and assertions
}
```

### Config Block (Optional)

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
  header "X-Custom-Header" = "value";
}
```

- `base_url`: Prepended to paths starting with `/`
- `header`: Default headers applied to all requests

### Variables

```
let username = "admin";
let userId = 42;
```

Use variables in paths and strings with `$name`:

```
GET "/api/users/$userId";
POST "/api/login" {
  body = "{ \"username\": \"$username\" }";
}
```

### HTTP Requests

#### GET / DELETE (Simple)

```
GET "/api/users/42";
DELETE "/api/users/42";
```

#### POST / PUT (With Body)

```
POST "/api/login" {
  header "Content-Type" = "application/json";
  body = "{ \"username\": \"admin\", \"password\": \"1234\" }";
}

PUT "/api/users/42" {
  body = "{ \"role\": \"ADMIN\" }";
}
```

### Assertions

```
expect status = 200;
expect header "Content-Type" = "application/json";
expect header "Content-Type" contains "json";
expect body contains "\"token\":";
expect status in 200..299;  // Bonus: range check
```

**Requirements:**
- Each test must have ≥1 request
- Each test must have ≥2 assertions

### Example Test

```
test Login {
  POST "/api/login" {
    body = "{ \"username\": \"admin\", \"password\": \"1234\" }";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"token\":";
}
```

## 🧪 Backend API Reference

The included Spring Boot backend provides these endpoints:

| Endpoint | Method | Description | Example Request |
|----------|--------|-------------|-----------------|
| `/api/login` | POST | Authenticate user | `{"username":"admin","password":"1234"}` |
| `/api/users/{id}` | GET | Get user by ID | - |
| `/api/users/{id}` | PUT | Update user | `{"role":"ADMIN"}` |
| `/api/users/{id}` | DELETE | Delete user | - |

### Manual Testing with cURL

```bash
# Login
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'

# Get user
curl http://localhost:8080/api/users/42

# Update user
curl -X PUT http://localhost:8080/api/users/42 \
  -H "Content-Type: application/json" \
  -d '{"role":"ADMIN"}'

# Delete user
curl -X DELETE http://localhost:8080/api/users/999
```

## 🔧 Advanced Usage

### Compile Specific Test File

```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar mytest.test MyTests.java
```

### Test Error Handling

```batch
compile-test.bat invalid.test
```

This demonstrates:
- Lexical errors (invalid identifiers)
- Syntax errors (missing semicolons)
- Validation errors (too few assertions)

## 🏗️ Architecture

### Lexer (JFlex)

- Tokenizes input into: keywords, identifiers, strings, numbers, operators
- Handles comments and whitespace
- Supports escape sequences in strings

### Parser (CUP)

- Builds an Abstract Syntax Tree (AST)
- Enforces grammar rules
- Provides error recovery and meaningful messages

### Code Generator

- Traverses the AST
- Generates JUnit 5 test methods
- Handles variable substitution
- Produces idiomatic Java code using `HttpClient`

### Generated Code Structure

```java
import org.junit.jupiter.api.*;
import java.net.http.*;

public class GeneratedTests {
    static HttpClient client;
    static String BASE = "http://localhost:8080";
    static Map<String,String> DEFAULT_HEADERS = new HashMap<>();

    @BeforeAll
    static void setup() {
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    }

    @Test
    void test_Login() throws Exception {
        // Build and send request
        // Assert response
    }
}
```

## ✨ Optional Features Implemented

- ✅ **Status range assertions**: `expect status in 200..299;`
- ✅ **Triple-quoted strings**: `"""multiline string"""`
- ✅ **Comprehensive error messages** with line/column info
- ✅ **Request-specific headers** override defaults
- ✅ **Validation** ensures tests have requests and assertions

## 📊 Example Output

### Successful Compilation

```
TestLang++ Compiler
===================
Input:  example.test
Output: GeneratedTests.java

Parsing...
✓ Parse successful

✓ Validation successful

Generating JUnit 5 code...
✓ Code generation successful

Generated 6 test(s)
Output written to: GeneratedTests.java
```

### Test Execution

```
========================================
Running Tests
========================================

Test run finished after 1234 ms
[         6 containers found      ]
[         0 containers skipped    ]
[         6 containers started    ]
[         0 containers aborted    ]
[         6 containers successful ]
[         0 containers failed     ]
[         6 tests found           ]
[         0 tests skipped         ]
[         6 tests started         ]
[         0 tests aborted         ]
[         6 tests successful      ]
[         0 tests failed          ]
```

## 🐛 Error Messages

### Lexical Error

```
Error: Line 10, Column 5: Illegal character <@>
```

### Syntax Error

```
Syntax error at line 12, column 3
```

### Validation Error

```
Validation error: Test 'Login' must contain at least 2 assertions (found 1)
```

## 🤝 Contributing

This is an academic project for SE2062. Contributions are welcome for educational purposes.

## 📄 License

MIT License - See [LICENSE](LICENSE) file

## 👨‍💻 Author

**Student ID:** IT23657250  
**Course:** SE2062 - Compilers  
**Assignment:** TestLang++ DSL Implementation

---

## 📹 Demo Video Script

1. **Introduction** (15s)
   - Show project structure
   - Explain what TestLang++ does

2. **Valid Test Compilation** (60s)
   - Show `example.test` file
   - Run `compile-test.bat example.test`
   - Show generated `GeneratedTests.java`

3. **Running Tests** (45s)
   - Start backend with `run-backend.bat`
   - Run `run-tests.bat`
   - Show all tests passing

4. **Error Handling** (30s)
   - Show `invalid.test`
   - Run `compile-test.bat invalid.test`
   - Show error messages

5. **Conclusion** (10s)
   - Recap features
   - Thank you

**Total Time:** ~2:40 minutes

---

**Happy Testing! 🚀**
TestLang++ (Java) – Backend API Testing DSL
