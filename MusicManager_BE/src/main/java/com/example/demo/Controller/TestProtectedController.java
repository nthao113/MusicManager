package com.example.demo.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/protected")
public class TestProtectedController {

    @GetMapping
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("Access Granted");
    }
}
