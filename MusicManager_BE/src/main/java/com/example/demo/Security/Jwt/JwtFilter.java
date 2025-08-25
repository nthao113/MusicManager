package com.example.demo.Security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Skip filter if request is ...
        String path = request.getRequestURI();
        System.out.println("Request path: " + path);

        if (path.startsWith("/auth") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/h2-console")) {

            System.out.println("Bypassed JwtFilter for path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        //Get token from header
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        //Check if header contains token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove the "Bearer" part
            try {
                username = jwtUtil.extractUsername(token); // get username from token
            } catch (Exception e) {
                // Invalid token, no processing required
            }
        }

        //If you have a username and no Authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //Check if token is valid with userDetails
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Assign Authentication to context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Allow filter chain to continue running
        filterChain.doFilter(request, response);
    }
}
