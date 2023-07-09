package com.portfolio.controller;

import com.portfolio.service.AuthorizationService;
import com.portfolio.service.RateLimitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShutDownController {

    @DeleteMapping(path = "/shut_down")
    @CrossOrigin
    public void shutDown(HttpServletRequest request) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return;
        }
        if (!AuthorizationService.authorize(request)) {
            return;
        }
        System.exit(0);
    }
}
