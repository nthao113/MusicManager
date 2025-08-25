package com.example.demo.Security;

import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void testRegisterNewUser() throws Exception {
        String json = """
            {
                "username": "newuser",
                "password": "password"
            }
        """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessProtectedEndpointAsUserWithValidToken() throws Exception {
        userRepository.save(new User("user", encoder.encode("123"), Role.ROLE_USER));

        String loginJson = """
        {
            "username": "user",
            "password": "123"
        }
    """;
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                        .andExpect(status().isOk())
                        .andReturn();

        String json = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(json).get("token").asText();

        mockMvc.perform(get("/api/playlists/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/users/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedEndpointAsAdminWithValidToken() throws Exception {
        userRepository.save(new User("admin", encoder.encode("123"), Role.ROLE_ADMIN));

        String loginJson = """
        {
            "username": "admin",
            "password": "123"
        }
    """;
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(json).get("token").asText();

        mockMvc.perform(get("/api/playlists/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/users/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
