package com.main.gundam.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/main", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
@RequiredArgsConstructor
public class RestApiController {

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }
    
    @GetMapping("/get/{userId}")
    public String get(@PathVariable(value = "userId") final String userId) {
        return userId;
    }
}
