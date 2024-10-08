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

import static com.lamdangfixbug.qmshoe.user.entity.Permission.*;
import static com.lamdangfixbug.qmshoe.user.entity.Role.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final String[] WHITE_LIST_URL = {
            "api/v1/auth/**",
            "api/v1/ratings/**",
            "api/v1/products/**",
            "api/v1/brands/**",
            "api/v1/colors/**",
            "api/v1/categories/**",
            "api/v1/sizes/**",
            "api/v1/payments/**",
            "api/v1/discounts/**",
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
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("/api/v1/management").hasAnyRole(ADMIN.name(), ACCOUNTANT.name(),ORDER_PROCESSOR.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyAuthority(READ.name(), ALL.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/management/orders/**").hasAnyAuthority(ORDER_CREATE.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/management/orders/**").hasAnyAuthority(ORDER_UPDATE.name())
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/management/orders/**").hasAnyAuthority(ORDER_UPDATE.name())
                                .requestMatchers(HttpMethod.PUT,"/api/v1/management/payments/**").hasAnyAuthority(PAYMENT_UPDATE.name())
                                .requestMatchers(HttpMethod.PATCH,"/api/v1/management/payments/**").hasAnyAuthority(PAYMENT_UPDATE.name())
                                .requestMatchers(HttpMethod.POST,"/api/v1/management/payments/**").hasAnyAuthority(PAYMENT_CREATE.name())
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
