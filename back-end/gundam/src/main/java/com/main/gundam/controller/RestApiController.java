package com.main.gundam.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/main", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
@RequiredArgsConstructor
@Slf4j
public class RestApiController {

    // private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    // @GetMapping("home")
    // public String home() {
    //     log.info("home");
    //     // logger.info("home");

    //     return "<h1>home</h1>";
    // }
    
    // @GetMapping("/get/{userId}")
    // public String get(@PathVariable(value = "userId") final String userId) {
    //     // logger.info("get user");
    //     log.info("get user");

    //     return userId;
    // }
}
