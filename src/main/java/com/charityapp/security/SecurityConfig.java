package com.charityapp.security;

import com.charityapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

/**
 * Configuration de sécurité de l'application
 * Cette classe définit les règles de sécurité, l'authentification et les autorisations
 * Elle configure également CORS, CSRF et la gestion des sessions
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Configuration principale de la chaîne de filtres de sécurité
     * Définit les règles d'accès, la gestion des sessions et les filtres
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        
        http
            // Désactive la protection CSRF pour l'API REST
            .csrf(csrf -> {
                csrf.disable();
                logger.info("CSRF protection disabled");
            })
            // Configure CORS pour permettre les requêtes cross-origin
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource());
                logger.info("CORS configuration applied");
            })
            // Définit les règles d'autorisation pour les endpoints
            .authorizeHttpRequests(auth -> {
                logger.info("Configuring authorization rules");
                auth
                    // Endpoints publics accessibles sans authentification
                    .requestMatchers("/api/auth/**", "/api/organisations/register", 
                                   "/api/organisations/validees", 
                                   "/api/categories", "/", "/login", "/register", 
                                   "/register-type", "/organisation/register", 
                                   "/donateur/register", "/home", "/css/**", 
                                   "/js/**", "/images/**", "/uploads/**").permitAll()
                    // Endpoints nécessitant une authentification
                    .requestMatchers("/api/utilisateurs/me", "/admin/**").authenticated()
                    // Tous les autres endpoints nécessitent une authentification
                    .anyRequest().authenticated();
                logger.info("Authorization rules configured");
            })
            // Configure la page de login personnalisée
            .formLogin(form -> {
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll();
                logger.info("Form login configured");
            })
            // Configure la déconnexion
            .logout(logout -> {
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
                logger.info("Logout configured");
            })
            // Configure la gestion des sessions comme stateless
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                logger.info("Session management configured to STATELESS");
            })
            // Configure le provider d'authentification
            .authenticationProvider(authenticationProvider())
            // Ajoute le filtre JWT avant le filtre d'authentification par défaut
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // Configure la gestion des erreurs d'authentification
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint((request, response, authException) -> {
                    logger.error("Authentication error: {}", authException.getMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                });
            });

        logger.info("Security filter chain configuration completed");
        return http.build();
    }

    /**
     * Configuration CORS pour permettre les requêtes cross-origin
     * Définit les origines, méthodes et headers autorisés
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configuration du provider d'authentification
     * Utilise UserDetailsService pour charger les utilisateurs
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("Configuring authentication provider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        logger.info("Authentication provider configured with NoOpPasswordEncoder");
        return authProvider;
    }

    /**
     * Configuration du gestionnaire d'authentification
     * Utilisé pour l'authentification des utilisateurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Configuring authentication manager");
        return config.getAuthenticationManager();
    }

    /**
     * Configuration de l'encodeur de mot de passe
     * Note: NoOpPasswordEncoder est utilisé pour le développement
     * À remplacer par BCryptPasswordEncoder en production
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Using NoOpPasswordEncoder for password encoding");
        return NoOpPasswordEncoder.getInstance();
    }
} 