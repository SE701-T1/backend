package com.team701.buddymatcher.services.impl;

import com.team701.buddymatcher.services.BasicService;
import org.springframework.stereotype.Service;

/**
 * Basic implementation for the BasicService which is a service providing a single endpoint to test that the server
 * is running correctly
 */
@Service
public class BasicServiceImpl implements BasicService {
    @Override
    public String helloWorld(String name) {
        return "Hello World, " + name;
    }
}
