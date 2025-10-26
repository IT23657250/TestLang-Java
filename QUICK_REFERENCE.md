# TestLang++ Quick Reference Card

## Syntax at a Glance

### Config Block (Optional)
```testlang
config {
  base_url = "http://localhost:8080";
  header "HeaderName" = "HeaderValue";
}
```

### Variables
```testlang
let varName = "string value";
let numberId = 42;
```

### Test Structure
```testlang
test TestName {
  // HTTP request
  // Assertions (minimum 2)
}
```

### HTTP Requests

#### Simple (GET, DELETE)
```testlang
GET "/api/path";
DELETE "/api/path/$id";
```

#### With Body (POST, PUT)
```testlang
POST "/api/path" {
  header "Custom" = "value";
  body = "{ \"key\": \"$var\" }";
};  // <-- semicolon required!

PUT "/api/path/$id" {
  body = "data";
};
```

### Assertions (Need ≥2 per test)
```testlang
expect status = 200;
expect status in 200..299;           // Bonus: range
expect header "Content-Type" = "application/json";
expect header "Content-Type" contains "json";
expect body contains "substring";
```

## Variable Substitution
```testlang
let userId = 42;
let name = "admin";

GET "/users/$userId";                // → "/users/42"
body = "{ \"name\": \"$name\" }";   // → { "name": "admin" }
```

## Complete Example
```testlang
config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
}

let user = "admin";
let password = "1234";

test LoginTest {
  POST "/api/login" {
    body = "{ \"username\": \"$user\", \"password\": \"$password\" }";
  };
  expect status = 200;
  expect body contains "token";
}
```

## Command Cheat Sheet

### Build Compiler
```batch
build.bat
```

### Compile .test File
```batch
compile-test.bat example.test GeneratedTests.java
```

### Start Backend
```batch
cd backend
mvn package
java -jar target\testlang-demo-backend-0.0.1-SNAPSHOT.jar
```

### Run Tests
```batch
run-tests.bat
```

## Common Errors

### Parse Error After Request Block
**Wrong:**
```testlang
POST "/path" {
  body = "data";
}  // ❌ Missing semicolon
```

**Correct:**
```testlang
POST "/path" {
  body = "data";
};  // ✅ Semicolon after closing brace
```

### Too Few Assertions
**Wrong:**
```testlang
test MyTest {
  GET "/api/test";
  expect status = 200;  // ❌ Only 1 assertion
}
```

**Correct:**
```testlang
test MyTest {
  GET "/api/test";
  expect status = 200;
  expect body contains "ok";  // ✅ At least 2 assertions
}
```

### Invalid Identifier
**Wrong:**
```testlang
let 2user = "admin";  // ❌ Starts with digit
```

**Correct:**
```testlang
let user2 = "admin";  // ✅ Starts with letter
```

## Generated JUnit Code Pattern

Input:
```testlang
test Example {
  GET "/api/test";
  expect status = 200;
  expect body contains "ok";
}
```

Output:
```java
@Test
void test_Example() throws Exception {
    HttpRequest.Builder b = HttpRequest.newBuilder(
        URI.create(BASE + "/api/test"))
        .timeout(Duration.ofSeconds(10))
        .GET();
    for (var e : DEFAULT_HEADERS.entrySet()) 
        b.header(e.getKey(), e.getValue());
    HttpResponse<String> resp = client.send(
        b.build(), 
        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    
    assertEquals(200, resp.statusCode());
    assertTrue(resp.body().contains("ok"));
}
```

## Backend Endpoints

| Method | Path | Request Body | Response |
|--------|------|--------------|----------|
| POST | `/api/login` | `{"username":"admin","password":"1234"}` | `{"success":true,"token":"..."}` |
| GET | `/api/users/{id}` | - | `{"id":42,"username":"user42",...}` |
| PUT | `/api/users/{id}` | `{"role":"ADMIN"}` | `{"updated":true,"role":"ADMIN"}` |
| DELETE | `/api/users/{id}` | - | `{"deleted":true,"id":42}` |

## File Structure
```
TestLang-Java/
├── example.test              ← Your test definitions
├── GeneratedTests.java       ← Compiler output
├── build.bat                 ← Build compiler
├── compile-test.bat          ← Compile .test → .java
├── run-tests.bat             ← Run JUnit tests
└── backend/                  ← Spring Boot server
```

## Workflow
```
1. Write DSL → example.test
2. Compile  → java -jar ... example.test GeneratedTests.java
3. Start    → java -jar backend/target/...jar
4. Test     → run-tests.bat
5. Success! → ✅ All tests pass
```

---

**Need Help?** See `README.md` or `EXECUTION_GUIDE.md`
