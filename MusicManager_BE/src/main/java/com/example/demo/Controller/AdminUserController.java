package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    //This is for security testing
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("Access Granted");
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delete a user by ID", description = "Delete a user from the system using the user's ID")
    @Parameter(name = "id", description = "ID of the user to be deleted", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        adminUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

