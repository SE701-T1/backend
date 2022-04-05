package com.team701.buddymatcher.interceptor;

import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Random;

public class UserInterceptorTest {

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Test
    void isValidURLTest() {
        UserInterceptor interceptor = new UserInterceptor();
        String url = "https://swagger.com/";
        Assertions.assertTrue(interceptor.isValidURL(url));
        url = "";
        Assertions.assertFalse(interceptor.isValidURL(url));
    }

    @Test
    void isValidTokenTest() {
        UserInterceptor interceptor = new UserInterceptor();
        String token = "";
        Assertions.assertNull(interceptor.isValidToken(token));
        token = "Bearer WrongToken";
        Assertions.assertNull(interceptor.isValidToken(token));
        User user = new User()
                .setId(new Random().nextLong())
                .setName("bored tester")
                .setEmail("boring.testing@gmail.com");
        token = "Bearer " + jwtTokenUtil.generateToken(user);
        Assertions.assertNotNull(interceptor.isValidToken(token));
    }
}
