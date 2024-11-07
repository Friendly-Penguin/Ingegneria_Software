package com.project.web.security;

//Sarà la prima linea di "sicurezza" con la quale le chiamate
//dovranno interfacciarsi

/**
 * This method intercepts incoming HTTP requests to extract and validate a JWT token from the "Authorization" header.
 * If the token is valid, it authenticates the user and sets the authentication context in Spring Security.
 * The process includes:
 * 1. Extracting the JWT token from the request header.
 * 2. Validating the token and extracting the username (email).
 * 3. Loading user details if no authentication context is found.
 * 4. Creating and setting the authentication token in the security context.
 * 5. Allowing the request to continue through the filter chain with the established authentication.
 *
 * This enables stateless authentication based on the JWT, ensuring that only valid requests are processed.
 */

import com.project.web.service.CustomUserDetailsService;
import com.project.web.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader =  request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            if(jwtUtils.isValidToken(jwtToken, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        filterChain.doFilter(request, response);

    }


}
