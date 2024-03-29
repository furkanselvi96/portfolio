package com.portfolio.controller;

import java.util.HashMap;

import com.portfolio.service.AuthorizationService;
import com.portfolio.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.entity.Stats;
import com.portfolio.service.StatsService;
import com.portfolio.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping(path = "/get", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getStats(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        Stats stats = statsService.getStats();
        if (stats == null) {
            return Response.serverError();
        }
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }

    @PostMapping(path = "/update/views", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateViews(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        Stats stats = statsService.updateViews();
        if (stats == null) {
            return Response.serverError();
        }
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }

    @PostMapping(path = "/update/last_updated", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateLastUpdated(HttpServletRequest request) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        Stats stats = statsService.updateLastUpdated();
        if (stats == null) {
            return Response.serverError();
        }
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }
}
