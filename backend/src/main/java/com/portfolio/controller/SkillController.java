package com.portfolio.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.entity.Skill;
import com.portfolio.service.AuthorizationService;
import com.portfolio.service.RateLimitService;
import com.portfolio.service.SkillService;
import com.portfolio.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PutMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newSkill(HttpServletRequest request, @RequestBody Skill skill) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = skillService.newSkill(skill);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{name}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteSkill(HttpServletRequest request, @PathVariable("name") String skillName) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = skillService.deleteSkill(skillName);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getSkills(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        HashMap<String, List<String>> skills = skillService.getSkills();
        return new ResponseEntity<>(Response.createBody("skills", skills), HttpStatus.OK);
    }

    @GetMapping(path = "/valid_types", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> validTypes(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        List<String> validTypes = skillService.validTypes();
        return new ResponseEntity<>(Response.createBody("validTypes", validTypes), HttpStatus.OK);
    }
    
}
