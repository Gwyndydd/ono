package com.fullstack.backend.ono.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/hello-world", produces = "application/json")
@RequiredArgsConstructor
public class HelloWorldController {

    private record StringResponse(String response) {}

    /**
     * covers the endpoint GET /hello-world
     * @return Hello World
     */
    @ApiResponse(responseCode = "200", description = "Hello World")
    @GetMapping
    public ResponseEntity<StringResponse> getHelloWorld() {
        log.info("Endpoint /hello-world reached");
        return ResponseEntity.ok(new StringResponse("Hello World!"));
    }

    /**
     * covers the endpoint GET /hello-world/name?name=
     * @return Hello + name
     */
    @GetMapping("/name")
    public ResponseEntity<StringResponse> getHelloName(@RequestParam String name) {
        log.info("Endpoint /hello-world/name?name reached with name {}", name);
        return ResponseEntity.ok(new StringResponse("Hello " + name + "!"));
    }
}