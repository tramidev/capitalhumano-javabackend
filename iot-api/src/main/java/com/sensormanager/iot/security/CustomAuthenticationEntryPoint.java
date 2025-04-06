package com.sensormanager.iot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensormanager.iot.dto.ApiResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ApiResponseDTO apiResponse = new ApiResponseDTO(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: Invalid username or password.");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}