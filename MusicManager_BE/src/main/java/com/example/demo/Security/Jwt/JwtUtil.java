package com.example.demo.Security.Jwt;

import com.example.demo.Model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get Username from token
    public String extractUsername(String token) {
        Claims claims = Jwts.parser() // Create a JWT parser
                .setSigningKey(SECRET.getBytes()) // Set the signing key to verify the token's signature
                .build() // Build the parser instance
                .parseClaimsJws(token) // Parse the token and extract the claims (payload)
                .getBody(); // Get the claims body
        return claims.getSubject(); // Return the "subject" field (username)
    }

    // Get Role from token
    public String extractRole(String token) {
        Claims claims = Jwts.parser() // Create a JWT parser
                .setSigningKey(SECRET.getBytes()) // Set the signing key to verify the token's signature
                .build() // Build the parser instance
                .parseClaimsJws(token) // Parse the token and extract the claims (payload)
                .getBody(); // Get the claims body
        return claims.get("role", String.class); // Return the custom "role" claim as a String
    }



    //Check for valid tokens
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // get username from token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // compare username and check token expiration
    }

    //Check expired token
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date()); // if expiration time < current time → expired
    }
}
