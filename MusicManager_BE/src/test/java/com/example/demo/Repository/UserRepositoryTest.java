package com.example.demo.Repository;

import com.example.demo.Model.Role;
import com.example.demo.Model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserRepositoryTest.TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void testFindByUsername_whenUserExists() {
        User user = new User("admin", encoder.encode("123"), Role.ROLE_USER);
        userRepository.save(user);

        User savedUser = userRepository.findByUsername("admin")
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("User Not Found!!!");
                });

        assertEquals(savedUser.getUsername(), "admin");
    }

    @Test
    public void testFindByUsername_whenUserNotExists() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentUser");

        assertThat(foundUser).isEmpty();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}

