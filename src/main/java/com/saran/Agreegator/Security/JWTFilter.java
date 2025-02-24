package com.saran.Agreegator.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saran.Agreegator.Dtos.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final MyUserDetailService myUserDetailService;
    private final ObjectMapper objectMapper = new ObjectMapper();// Inject ObjectMapper

    public JWTFilter(JWTUtils jwtUtils, MyUserDetailService myUserDetailService ) {
        this.jwtUtils = jwtUtils;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();


        if (requestURI.startsWith("/api/auth/") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/v3/api-docs/") ||
                requestURI.equals("/swagger-ui.html") ||
                requestURI.equals("/v3/api-docs/**")) {
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader = request.getHeader("Authorization");
        String token = "";
        String email = "";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtUtils.extractUserName(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailService.loadUserByUsername(email);
            if (jwtUtils.validateToken(token, userDetails)) {
                log.info("Token is valid: {}", email);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occurred in JWTFilter: {}", e.getMessage());

            // ✅ Send an error response using ObjectMapper
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            Response errorResponse = Response.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("JWT Filter Error: " + e.getMessage())
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
