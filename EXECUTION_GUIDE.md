# TestLang++ - Step-by-Step Execution Guide

## Prerequisites Verification

Before starting, ensure you have:
- ✅ Java JDK 11 or higher installed
- ✅ Maven 3.6+ installed
- ✅ Both `java` and `mvn` in your PATH

Verify with:
```batch
java -version
mvn -version
```

## Step 1: Build the TestLang++ Compiler

```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
build.bat
```

**Expected Output:**
```
========================================
Building TestLang++ Compiler
========================================

... Maven build output ...

========================================
Build successful!
========================================

JAR file: target\testlang-java-1.0.0-jar-with-dependencies.jar
```

**What this does:**
- Generates lexer from `src/main/jflex/lexer.flex` using JFlex
- Generates parser from `src/main/cup/parser.cup` using CUP
- Compiles all Java sources
- Creates executable JAR with all dependencies

## Step 2: Build the Reference Backend

Open a **NEW terminal window** and run:

```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java\backend
mvn clean package
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
...
```

This creates: `backend/target/testlang-demo-backend-0.0.1-SNAPSHOT.jar`

## Step 3: Start the Backend Server

In the same terminal (from Step 2):

```batch
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
...
Started TestLangBackendApplication in X.XXX seconds
```

**IMPORTANT:** Keep this terminal window open! The server must be running.

The server is now available at: `http://localhost:8080`

## Step 4: Compile a .test File

Back in your **FIRST terminal**:

```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
compile-test.bat example.test GeneratedTests.java
```

**Expected Output:**
```
========================================
Compiling TestLang++ file
========================================
Input:  example.test
Output: GeneratedTests.java

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

========================================
Compilation successful!
========================================
```

**What this does:**
- Reads `example.test`
- Parses the DSL using our generated lexer and parser
- Generates `GeneratedTests.java` with JUnit 5 test methods

## Step 5: Review the Generated Code

Open and examine `GeneratedTests.java`:

```batch
type GeneratedTests.java
```

You should see:
- Import statements for JUnit 5 and HttpClient
- A `GeneratedTests` class with `@BeforeAll` setup
- Multiple `@Test` methods (`test_Login`, `test_GetUser`, etc.)
- HTTP requests using `HttpClient`
- Assertions using JUnit 5's `assertEquals` and `assertTrue`

## Step 6: Compile the Generated Tests

```batch
mvn dependency:copy-dependencies -DoutputDirectory=lib
mkdir test-output
javac -cp "lib\*" -d test-output GeneratedTests.java
```

**Expected Output:**
```
(No output means success)
```

## Step 7: Run the Tests

First, ensure the backend is still running (Step 3).

Then run:

```batch
REM Download JUnit Platform Console Standalone (one time only)
mvn dependency:get -Dartifact=org.junit.platform:junit-platform-console-standalone:1.9.3:jar -Ddest=lib\junit-platform-console-standalone.jar

REM Run the tests
java -jar lib\junit-platform-console-standalone.jar --class-path test-output --scan-class-path
```

**Expected Output:**
```
Thanks for using JUnit! Support its development at https://junit.org/sponsoring

╷
├─ JUnit Jupiter ✔
│  ├─ GeneratedTests ✔
│  │  ├─ test_Login() ✔
│  │  ├─ test_GetUser() ✔
│  │  ├─ test_UpdateUser() ✔
│  │  ├─ test_DeleteUser() ✔
│  │  ├─ test_LoginWithMultipleChecks() ✔
│  │  └─ test_CheckStatusRange() ✔

Test run finished after XXXX ms
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

## Step 8: Test Error Handling

```batch
compile-test.bat invalid.test
```

**Expected Output:**
```
TestLang++ Compiler
===================
Input:  invalid.test
Output: GeneratedTests.java

Parsing...
Syntax error at line 10, column 5
Fatal syntax error at line 10, column 5
Error: Parse error
```

This demonstrates meaningful error messages for invalid DSL syntax.

## Manual Backend Testing (Optional)

You can manually test the backend endpoints:

```batch
REM Login
curl -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"1234\"}"

REM Get user
curl http://localhost:8080/api/users/42

REM Update user
curl -X PUT http://localhost:8080/api/users/42 -H "Content-Type: application/json" -d "{\"role\":\"ADMIN\"}"

REM Delete user
curl -X DELETE http://localhost:8080/api/users/999
```

## Automated Scripts

For convenience, the following scripts are provided:

| Script | Purpose |
|--------|---------|
| `build.bat` | Build the compiler |
| `compile-test.bat <input> [output]` | Compile a .test file |
| `run-backend.bat` | Build and start the backend |
| `run-tests.bat` | Compile and run generated tests |
| `demo.bat` | Complete end-to-end workflow |
| `test-invalid.bat` | Test error handling |

## Quick Demo (All-in-One)

### Terminal 1:
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
build.bat
```

### Terminal 2:
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java\backend
mvn package
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
```

Wait for "Started TestLangBackendApplication"

### Terminal 1 (continued):
```batch
compile-test.bat example.test GeneratedTests.java
run-tests.bat
```

## Troubleshooting

### "Backend not running" errors in tests
- Ensure Step 3 (backend) is running in a separate terminal
- Check that you see "Started TestLangBackendApplication"
- Verify: `curl http://localhost:8080/api/login`

### "Parse error" when compiling .test files
- Check syntax: POST/PUT with request blocks need `;` after the `}`
  ```
  POST "/path" {
    body = "data";
  };  // <-- semicolon required here!
  ```

### "BUILD FAILURE" during Maven build
- Ensure Java 11+ is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Try: `mvn clean install`

### JUnit tests not found
- Ensure `test-output` directory exists
- Verify `GeneratedTests.class` is in `test-output`
- Check classpath: `dir test-output`

## Project Structure Reference

```
TestLang-Java/
├── src/main/
│   ├── jflex/lexer.flex          ← Lexical analyzer
│   ├── cup/parser.cup             ← Parser grammar
│   └── java/com/testlang/
│       ├── Main.java              ← Compiler entry point
│       ├── ast/                   ← AST node classes
│       └── codegen/               ← JUnit code generator
├── backend/                        ← Spring Boot backend
├── example.test                    ← Main test file
├── invalid.test                    ← Error demo file
├── GeneratedTests.java             ← Output (generated)
├── *.bat                           ← Helper scripts
└── README.md                       ← Documentation
```

## Video Demo Checklist

For your 3-minute demo video, follow this script:

1. **[15s] Introduction**
   - Show project folder structure
   - Explain: "TestLang++ compiles DSL test files to JUnit 5"

2. **[60s] Valid Test Compilation**
   - Open `example.test` in editor, highlight features:
     - config block with base_url
     - variables with $ substitution
     - POST with body
     - expect assertions
   - Run: `compile-test.bat example.test`
   - Show generated `GeneratedTests.java`
   - Point out: HttpClient usage, JUnit annotations

3. **[45s] Running Tests**
   - Show backend already running in separate terminal
   - Run: `run-tests.bat`
   - Show all 6 tests passing (green checkmarks)

4. **[30s] Error Handling**
   - Open `invalid.test`, point out errors:
     - Invalid identifier starting with digit
     - Missing semicolon
   - Run: `compile-test.bat invalid.test`
   - Show error messages with line numbers

5. **[10s] Conclusion**
   - Summary: "Full DSL implementation with lexer, parser, code generation"
   - "All bonus features implemented: status ranges, multiline strings"
   - Thank you

---

## Success Criteria Checklist

- ✅ Lexer recognizes all tokens (keywords, identifiers, strings, numbers, comments)
- ✅ Parser builds correct AST for config, variables, tests, requests, assertions
- ✅ Code generator produces compilable JUnit 5 code
- ✅ Generated tests use java.net.http.HttpClient (no third-party libraries)
- ✅ Variable substitution works ($var in strings/paths)
- ✅ All assertion types work (status, header equals/contains, body contains)
- ✅ Meaningful error messages with line/column info
- ✅ Backend provides all required endpoints
- ✅ End-to-end workflow: DSL → Parser → Java → JUnit → Pass/Fail
- ✅ Bonus: Status range assertions (expect status in 200..299)
- ✅ Bonus: Triple-quoted strings support
- ✅ Documentation: README with complete instructions
- ✅ Demo-ready: All scripts work, examples compile and run

**You're ready to demo!** 🎉
