# TestLang++ Demo Video Script (3 Minutes)

---

## 🎬 INTRODUCTION (0:00 - 0:20) - 20 seconds

### [VISUAL: Show desktop with project folder]

**"Hello! I'm presenting TestLang++, a Domain-Specific Language for HTTP API testing that I developed for SE2062.**

**This compiler takes test descriptions written in a custom DSL and generates executable JUnit 5 tests using Java's built-in HttpClient.**

**Let me show you how it works end-to-end."**

### [VISUAL: Open project folder, show file structure briefly]

---

## 📝 PART 1: LANGUAGE FEATURES (0:20 - 1:20) - 60 seconds

### [VISUAL: Open example.test in editor]

**"Here's an example test file written in TestLang++. Let me highlight the key features:**

### [VISUAL: Scroll through example.test, pointing to each section]

**"First, we have a config block where we define the base URL and default headers that apply to all requests."**

```
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}
```

**"Next, we can declare variables - both strings and integers - which we can use throughout our tests."**

```
let user = "admin";
let id = 42;
```

**"Now for the actual tests. Each test block contains HTTP requests and assertions."**

### [VISUAL: Highlight the Login test]

**"Here's a POST request to a login endpoint. Notice the variable substitution with the dollar sign - we're using the 'user' variable we declared earlier. The request has a JSON body."**

```
test Login {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"1234\" }";
  };
```

**"After the request, we have multiple assertions - checking the status code, headers, and response body content. Each test must have at least two assertions."**

```
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\"token\":";
}
```

**"The DSL supports all four major HTTP methods - GET, POST, PUT, and DELETE - along with various assertion types."**

### [VISUAL: Scroll to show GET and PUT examples]

**"I've also implemented bonus features like status range checking."**

```
expect status in 200..299;
```

**"This test file has six different test cases that will compile into six separate JUnit test methods."**

---

## ⚙️ PART 2: COMPILATION & CODE GENERATION (1:20 - 2:10) - 50 seconds

### [VISUAL: Switch to terminal]

**"Now let's compile this DSL file. I'll run the compiler..."**

### [VISUAL: Type and run command]

```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar example.test GeneratedTests.java
```

### [VISUAL: Show compiler output]

**"The compiler parses the DSL, validates it, and successfully generates Java code. It created 6 tests from our DSL file."**

### [VISUAL: Open GeneratedTests.java]

**"Here's the generated JUnit 5 code. Notice how clean and idiomatic it is.**

**The compiler created a test class with proper JUnit annotations, a setup method that initializes the HttpClient, and individual test methods."**

### [VISUAL: Scroll through generated code, highlight key parts]

**"Each test method uses Java 11's HttpClient API - no third-party libraries like RestAssured or Selenium."**

**"The variable substitution worked - you can see 'admin' is now hardcoded where we used the variable, and the user ID is '42'."**

**"All the assertions use standard JUnit 5 methods like assertEquals and assertTrue."**

---

## ✅ PART 3: RUNNING TESTS (2:10 - 2:40) - 30 seconds

### [VISUAL: Switch to second terminal showing backend]

**"To test the generated code, I have a Spring Boot backend running on localhost:8080."**

### [VISUAL: Show backend startup message]

**"Now let's compile and run the generated tests..."**

### [VISUAL: Switch back to first terminal, run command]

```batch
.\run-tests.bat
```

### [VISUAL: Show test execution and results]

**"And there we go! All six tests executed successfully!**

**We can see the test names - Login, GetUser, UpdateUser, DeleteUser, LoginWithMultipleChecks, and CheckStatusRange.**

**Six tests found, six tests successful, zero failures. The generated code works perfectly!"**

---

## 🚫 PART 4: ERROR HANDLING (2:40 - 2:55) - 15 seconds

### [VISUAL: Type command to compile invalid.test]

**"Finally, let me demonstrate error handling by compiling an intentionally invalid test file."**

```batch
java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar invalid.test
```

### [VISUAL: Show error output]

**"The compiler correctly identifies the syntax error on line 9, column 5, where an identifier starts with a digit - which isn't allowed.**

**The error message shows the exact location and the stack trace for debugging."**

---

## 🎓 CONCLUSION (2:55 - 3:00) - 5 seconds

**"To summarize: TestLang++ provides a complete DSL implementation with a lexer built using JFlex, a parser using CUP, and a code generator that produces idiomatic JUnit 5 tests.**

**All required features are implemented, plus bonus features like status range assertions.**

**Thank you!"**

### [VISUAL: Fade out or show final screen with project name]

---

## 📋 PREPARATION CHECKLIST

Before recording:

### Terminal 1 Setup:
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java
# Make sure compiler is built
# Close any unnecessary windows
```

### Terminal 2 Setup (Backend):
```batch
cd c:\Users\nisath\Dropbox\PC\Desktop\TestLang-Java\backend
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
# Wait for "Started TestLangBackendApplication"
```

### Files to Have Ready:
- ✅ `example.test` - Open in editor
- ✅ `GeneratedTests.java` - Ready to open
- ✅ Terminal 1 - At project root
- ✅ Terminal 2 - Backend running

### Commands to Copy-Paste:
1. Compile: `java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar example.test GeneratedTests.java`
2. Run tests: `.\run-tests.bat`
3. Show errors: `java -jar target\testlang-java-1.0.0-jar-with-dependencies.jar invalid.test`

---

## 🎥 RECORDING TIPS

### Setup:
1. **Close unnecessary applications** (Discord, Spotify, etc.)
2. **Increase terminal font size** for readability
3. **Use a clean desktop background**
4. **Test your microphone** beforehand
5. **Do a practice run** once

### During Recording:
1. **Speak clearly and at a steady pace**
2. **Pause briefly** when switching screens
3. **Use cursor/mouse** to highlight important parts
4. **Don't rush** - you have 3 minutes
5. **If you make a mistake**, stop and restart (don't try to edit)

### Screen Recording Settings:
- **Resolution**: 1920x1080 or 1280x720
- **Frame rate**: 30 fps
- **Audio**: Clear voice, no background noise
- **Format**: MP4 (most compatible)

### Tools You Can Use:
- **OBS Studio** (free, professional)
- **Windows Game Bar** (Win + G)
- **ShareX** (free, easy to use)
- **Camtasia** (if available)

---

## 🎯 KEY POINTS TO EMPHASIZE

1. **Language Design**
   - "Custom DSL with config, variables, tests"
   - "Supports GET, POST, PUT, DELETE"
   - "Variable substitution with $ syntax"

2. **Implementation**
   - "Lexer built with JFlex"
   - "Parser built with CUP"
   - "Generates idiomatic Java code"

3. **Testing**
   - "Uses only Java 11 HttpClient - no third-party libraries"
   - "Pure JUnit 5 - no Selenium or RestAssured"
   - "All 6 tests pass successfully"

4. **Error Handling**
   - "Meaningful error messages"
   - "Precise line and column numbers"
   - "Graceful failure"

5. **Bonus Features**
   - "Status range assertions (200..299)"
   - "Triple-quoted strings support"
   - "Complete end-to-end workflow"

---

## ⏱️ TIME MANAGEMENT

| Section | Duration | Cumulative |
|---------|----------|------------|
| Introduction | 20s | 0:20 |
| Language Features | 60s | 1:20 |
| Compilation & Code Gen | 50s | 2:10 |
| Running Tests | 30s | 2:40 |
| Error Handling | 15s | 2:55 |
| Conclusion | 5s | 3:00 |

**Total: 3:00 exactly**

If you're running over time:
- Skip showing the config block in detail (5-10s saved)
- Don't scroll through ALL of GeneratedTests.java (10s saved)
- Speed up the conclusion (save 2-3s)

If you're under time:
- Show more examples from example.test
- Highlight more features in GeneratedTests.java
- Mention technical details (AST, code generator architecture)

---

## 💡 ALTERNATIVE OPENING (More Technical)

**"Hi! For my Compilers course project, I built TestLang++ - a complete DSL implementation for HTTP API testing.**

**The system consists of three main components:**
**1. A lexical analyzer built with JFlex that tokenizes the input**
**2. A syntax analyzer built with CUP that constructs an Abstract Syntax Tree**
**3. A code generator that produces JUnit 5 tests using Java's HttpClient**

**Let me demonstrate the complete workflow..."**

---

## 💡 ALTERNATIVE ENDING (More Formal)

**"In conclusion, this project demonstrates:**
**- Complete language design from specification to implementation**
**- Professional use of compiler construction tools - JFlex and CUP**
**- Code generation producing production-quality Java**
**- Comprehensive error handling with meaningful diagnostics**

**All requirements have been met, including bonus features.**

**Thank you for watching!"**

---

## 📝 BACKUP PLAN

If something goes wrong during recording:

### If backend crashes:
- Restart it in Terminal 2
- Mention: "I have a Spring Boot backend running that provides the test endpoints"

### If tests fail:
- Check backend is running
- Restart backend
- Re-record this section

### If compilation fails:
- Make sure you're in the right directory
- Check the command is typed correctly
- Worst case: show a pre-compiled GeneratedTests.java

### If you freeze/forget:
- Take a breath
- Restart from the beginning of that section
- Don't try to continue if flustered

---

## ✅ FINAL CHECKS

Before submitting:

- ✅ Video is under 3 minutes
- ✅ Audio is clear (no background noise)
- ✅ Screen is visible (text is readable)
- ✅ All features demonstrated
- ✅ Error handling shown
- ✅ Tests run successfully
- ✅ Professional presentation
- ✅ Video format is compatible (MP4)

---

**Good luck with your recording! You've got this! 🎬**
