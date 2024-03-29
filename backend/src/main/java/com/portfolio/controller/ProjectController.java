package com.portfolio.controller;

import java.util.HashMap;
import java.util.List;

import com.portfolio.service.ProjectService;
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

import com.portfolio.entity.Project;
import com.portfolio.service.AuthorizationService;
import com.portfolio.service.RateLimitService;
import com.portfolio.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;

    @PutMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newProject(HttpServletRequest request, @RequestBody Project project) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = projectService.newProject(project);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateProject(HttpServletRequest request, @RequestBody Project updateProject, @PathVariable("id") Integer id) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = projectService.updateProject(updateProject, id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteProject(HttpServletRequest request, @PathVariable("id") Integer id) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = projectService.deleteProject(id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getProjects(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        List<Project> projects = projectService.getProjects();
        return new ResponseEntity<>(Response.createBody("projects", projects), HttpStatus.OK);
    }
}