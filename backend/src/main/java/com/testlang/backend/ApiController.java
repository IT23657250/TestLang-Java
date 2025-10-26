package com.testlang.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * POST /api/login - Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if ("admin".equals(username) && "1234".equals(password)) {
            response.put("success", true);
            response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.demo");
            response.put("username", username);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(response);
        } else {
            response.put("success", false);
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * GET /api/users/{id} - Get user by ID
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable int id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("role", "USER");
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .body(user);
    }

    /**
     * PUT /api/users/{id} - Update user
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable int id, 
            @RequestBody Map<String, String> updates) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("updated", true);
        response.put("id", id);
        response.put("role", updates.get("role"));
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("X-App", "TestLangDemo")
                .body(response);
    }

    /**
     * DELETE /api/users/{id} - Delete user
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        response.put("deleted", true);
        response.put("id", id);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }
}
