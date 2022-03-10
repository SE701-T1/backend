package com.team701.buddymatcher.services.impl;

import com.team701.buddymatcher.services.BasicService;
import org.springframework.stereotype.Service;

@Service
public class BasicServiceImpl implements BasicService {
    @Override
    public String helloWorld(String name) {
        return "Hello World, " + name;
    }
}
