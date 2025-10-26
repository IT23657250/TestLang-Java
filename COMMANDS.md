# TestLang++ - Step-by-Step Commands

## Prerequisites Check

First, verify you have the required tools installed:

```batch
java -version
```
Should show Java 11 or higher.

```batch
mvn -version
```
Should show Maven 3.6 or higher.

---

## Step 1: Build the TestLang++ Compiler

Open PowerShell and navigate to the project directory:

```powershell
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
```

Build the compiler:

```batch
.\build.bat
```

**Expected Result:**
- You should see Maven downloading dependencies
- JFlex generates the lexer
- CUP generates the parser
- Java files compile successfully
- Final message: "Build successful!"
- JAR created at: `target\testlang-java-1.0.0-jar-with-dependencies.jar`

---

## Step 2: Build the Backend Server

In the same terminal:

```powershell
cd backend
```

```batch
mvn clean package
```

**Expected Result:**
- Maven builds the Spring Boot application
- Message: "BUILD SUCCESS"
- JAR created at: `target\testlang-demo-backend-0.0.1-SNAPSHOT.jar`

---

## Step 3: Start the Backend Server

**IMPORTANT:** Open a **NEW PowerShell window** (keep the first one open).

In the new window:

```powershell
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java\backend
```

```batch
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
```

**Expected Result:**
- You'll see Spring Boot ASCII art logo
- Server starts up
- Final message: "Started TestLangBackendApplication in X.XXX seconds"
- Server is now running on: http://localhost:8080

**KEEP THIS WINDOW OPEN!** Don't close it. The server must stay running.

---

## Step 4: Compile a Test File

Go back to your **FIRST PowerShell window**.

Navigate back to the root directory:

```powershell
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
```

Compile the example test file:

```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar example.test GeneratedTests.java
```

**Expected Result:**
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

**What happened:**
- Compiler read `example.test`
- Parsed the DSL
- Generated `GeneratedTests.java` in the current directory

---

## Step 5: View the Generated Tests (Optional)

You can view the generated Java code:

```batch
type GeneratedTests.java
```

Or open it in your editor to see the JUnit 5 test methods.

---

## Step 6: Compile and Run the Generated Tests

Still in the first PowerShell window, run:

```batch
.\run-tests.bat
```

**This script will:**
1. Download JUnit dependencies
2. Compile `GeneratedTests.java`
3. Run all the JUnit tests

**Expected Result:**
```
========================================
Compiling Generated Tests
========================================

Compiling GeneratedTests.java with JUnit...
Compilation successful!

========================================
Running Tests
========================================
Make sure the backend is running on http://localhost:8080

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
[         6 tests found           ]
[         6 tests successful      ]
[         0 tests failed          ]
```

**🎉 SUCCESS!** All 6 tests passed!

---

## Step 7: Test Error Handling (Optional)

In the first window, try compiling an invalid test file:

```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar invalid.test
```

**Expected Result (THIS IS CORRECT - IT SHOULD FAIL):**
```
TestLang++ Compiler
===================
Input:  invalid.test
Output: GeneratedTests.java

Parsing...
Syntax error at line 9, column 5
Fatal syntax error at line 9, column 5
Error: Parse error
java.lang.Exception: Parse error
        at com.testlang.parser.parser.unrecovered_syntax_error(parser.java:217)
        at java_cup.runtime.lr_parser.parse(lr_parser.java:731)
        at com.testlang.Main.main(Main.java:45)
```

**✅ This error is EXPECTED!** The `invalid.test` file contains intentional errors:
- Line 9: `let 2invalid = "test";` - Identifier cannot start with a digit
- Line 14: Missing semicolon after GET request
- Only 1 assertion (needs at least 2)

This demonstrates that your compiler has **meaningful error messages** with precise line and column numbers!

---

## Quick Commands Summary

### One-Time Setup:
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
.\build.bat
cd backend
mvn package
```

### Every Time You Want to Run Tests:

**Terminal 1:**
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar example.test GeneratedTests.java
.\run-tests.bat
```

**Terminal 2 (keep running):**
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java\backend
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
```

---

## Alternative: Use the Automated Scripts

We have convenience scripts:

### Build compiler:
```batch
.\build.bat
```

### Start backend:
```batch
.\run-backend.bat
```

### Compile a test file:
```batch
.\compile-test.bat example.test GeneratedTests.java
```

### Run tests:
```batch
.\run-tests.bat
```

---

## Testing Different Scenarios

### Compile and test a simple GET request:
```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar minimal.test GeneratedTests.java
.\run-tests.bat
```

### Compile and test a POST request:
```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar post.test GeneratedTests.java
.\run-tests.bat
```

---

## Troubleshooting

### If backend won't start:
```batch
# Check if port 8080 is already in use
netstat -ano | findstr :8080

# If something is running on 8080, kill it:
# Find the PID from the command above, then:
taskkill /PID <PID> /F
```

### If tests fail with "Connection refused":
- Make sure backend is running (Terminal 2)
- Check you see "Started TestLangBackendApplication"
- Try accessing: http://localhost:8080/api/users/42 in a browser

### If compilation fails:
```batch
# Clean and rebuild everything:
mvn clean
.\build.bat
```

### If run-tests.bat fails:
```batch
# Manual compilation:
mvn dependency:copy-dependencies -DoutputDirectory=lib
mkdir test-output
javac -cp "lib\*" -d test-output GeneratedTests.java
java -jar lib\junit-platform-console-standalone.jar --class-path test-output --scan-class-path
```

---

## For Your Demo Video

Run these commands in order while recording:

1. Show project structure:
   ```batch
   dir
   ```

2. Show example.test file:
   ```batch
   type example.test
   ```

3. Compile it:
   ```batch
   java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar example.test GeneratedTests.java
   ```

4. Show generated code:
   ```batch
   type GeneratedTests.java
   ```

5. Run tests (make sure backend is running in another window):
   ```batch
   .\run-tests.bat
   ```

6. Show error handling:
   ```batch
   java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar invalid.test
   ```

---

## Clean Up After Testing

Stop the backend:
- In Terminal 2, press `Ctrl+C`

Clean generated files:
```batch
del GeneratedTests.java
rmdir /s /q test-output
rmdir /s /q lib
```

---

## Next Steps

1. ✅ Test the complete workflow using these commands
2. ✅ Make sure all 6 tests pass
3. ✅ Record your demo video showing the workflow
4. ✅ Submit your project

Good luck! 🚀
