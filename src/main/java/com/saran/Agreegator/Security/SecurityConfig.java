package com.saran.Agreegator.Security;

import com.saran.Agreegator.Exceptions.CustomAccessDeniedHandler;
import com.saran.Agreegator.Exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private  JWTFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;

        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAccessDeniedHandler customAccessDeniedException) throws Exception {
        http.csrf(customizer->customizer.disable()); // in default spring security create csrf token in each req to prevent csrf attacks. In this case csrf is disabled and csrf attack is prevented by creating new SessionId in each req.
        http.cors(Customizer.withDefaults());
        http.exceptionHandling(exception->exception.accessDeniedHandler(customAccessDeniedException).authenticationEntryPoint(customAuthenticationEntryPoint));//used to whenever user or manager access to admin pages or urls then this exception should thrown with custom message
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean // this bean is used to change the default authentication provider
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // most used authentication provider for DB verification
        provider.setUserDetailsService(userDetailsService); // userdetailservice(Interface) is responsible for verify the user details so we need to create own class adn it is also interface
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // verify the pass after hashing
        return provider;
    }

    // authentication manager is responsible for verifying the login details so we do our custom impl
    // authentication manager calls the authentication provider
    // refer flowchart image in spring learnings folder
    @Bean // Responsible for Login
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
