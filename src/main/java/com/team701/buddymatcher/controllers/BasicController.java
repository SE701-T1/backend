package com.team701.buddymatcher.controllers;

import com.team701.buddymatcher.services.BasicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Basic controller for handling of user requests, this is only for testing to ensure the server is running properly
 */
@RestController
@Api
@RequestMapping("/api/basic")
public class BasicController {

    private final BasicService basicService;

    @Autowired
    public BasicController(BasicService basicService) {
        this.basicService = basicService;
    }

    @ApiOperation("Basic get method that returns a hello world")
    @GetMapping(path = "/hello/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hello(@PathVariable("name") String name) {
        String response = basicService.helloWorld(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
