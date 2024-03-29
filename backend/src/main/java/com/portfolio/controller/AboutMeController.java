package com.portfolio.controller;

import java.util.HashMap;

import com.portfolio.service.AboutMeService;
import com.portfolio.service.AuthorizationService;
import com.portfolio.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.entity.AboutMe;
import com.portfolio.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/about_me")
public class AboutMeController {
    
    @Autowired
    private AboutMeService aboutMeService;

    @PutMapping(path = "/update", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> setAboutMe(HttpServletRequest request, @RequestBody AboutMe aboutMe) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = aboutMeService.setAboutMe(aboutMe);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getAboutMe(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        return new ResponseEntity<>(Response.createBody("text", aboutMeService.getAboutMe()), HttpStatus.OK);
    }
}
