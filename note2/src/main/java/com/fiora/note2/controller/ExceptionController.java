package com.fiora.note2.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController implements ErrorController {
    private static final String DEFAULT_PATH = "/error";
    @Override
    public String getErrorPath() {
        return DEFAULT_PATH;
    }

    @RequestMapping(value = DEFAULT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> error() {
        return ResponseEntity.status(500).build();
    }
}
