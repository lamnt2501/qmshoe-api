package com.lamdangfixbug.qmshoe.configurations;

import com.lamdangfixbug.qmshoe.auth.filter.ExceptionHandlerFilter;
import com.lamdangfixbug.qmshoe.auth.filter.JwtAuthenticationFilter;
import com.lamdangfixbug.qmshoe.user.entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import static com.lamdangfixbug.qmshoe.user.entity.Role.ADMIN;
import static com.lamdangfixbug.qmshoe.user.entity.Role.MANAGER;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.ADMIN_ALL;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.ADMIN_READ;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.ADMIN_CREATE;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.ADMIN_DELETE;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.ADMIN_UPDATE;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.MANAGER_ALL;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.MANAGER_CREATE;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.MANAGER_DELETE;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.MANAGER_READ;
import static com.lamdangfixbug.qmshoe.user.entity.Permission.MANAGER_UPDATE;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final String[] WHITE_LIST_URL = {
            "api/v1/auth/**",
            "api/v1/products/**",
            "api/v1/brands/**",
            "api/v1/colors/**",
            "api/v1/categories/**",
            "api/v1/sizes/**",
            "api/v1/payments/**",
            //------
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui/**",
    };

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          ExceptionHandlerFilter exceptionHandlerFilter,
                          @Qualifier("customAuthenticationEntryPoint") AuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyAuthority(MANAGER_ALL.name(), ADMIN_ALL.name(), MANAGER_READ.name(), ADMIN_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/management/**").hasAnyAuthority(MANAGER_ALL.name(), ADMIN_ALL.name(), MANAGER_CREATE.name(), ADMIN_CREATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAnyAuthority(MANAGER_DELETE.name(), ADMIN_DELETE.name(), ADMIN_ALL.name(), MANAGER_ALL.name())
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/management/**").hasAnyAuthority(MANAGER_UPDATE.name(), ADMIN_UPDATE.name(), ADMIN_ALL.name(), MANAGER_ALL.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAnyAuthority(MANAGER_ALL.name(), ADMIN_ALL.name(), ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, CorsFilter.class);
//                .exceptionHandling(e->e.authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
