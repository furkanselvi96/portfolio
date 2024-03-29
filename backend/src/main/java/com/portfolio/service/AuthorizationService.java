package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    private static String appPassword;

    @Value("${com.jasonpyau.appPassword}")
    @SuppressWarnings("static-access")
    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    private AuthorizationService() {}

    public static boolean authorize(HttpServletRequest request) {
        String password = request.getHeader("Authorization");
        return (password != null && !password.isBlank() && password.equals(appPassword));
    }
}
