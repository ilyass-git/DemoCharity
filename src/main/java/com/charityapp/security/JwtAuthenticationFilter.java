package com.charityapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    // Liste des endpoints publics qui ne nécessitent pas d'authentification
    private final List<String> publicEndpoints = Arrays.asList(
        "/api/auth",
        "/api/organisations/register",
        "/api/organisations/validees",
        "/api/actions",
        "/api/categories",
        "/",
        "/login",
        "/register",
        "/register-type",
        "/organisation/register",
        "/donateur/register",
        "/home",
        "/css/",
        "/js/",
        "/images/",
        "/oauth2/",
        "/login/oauth2/"
    );

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.info("Processing {} request for URI: {}", method, requestURI);
        
        // Laisser passer toutes les requêtes sans vérification
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicEndpoint(String requestURI) {
        boolean isPublic = publicEndpoints.stream().anyMatch(endpoint -> requestURI.startsWith(endpoint));
        logger.info("Checking if endpoint is public: {} - Result: {}", requestURI, isPublic);
        return isPublic;
    }
} 