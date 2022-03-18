package com.team701.buddymatcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {
        "socketio.host=localhost",
        "socketio.port=8081"
})
class BuddyMatcherApplicationTests {

    @Test
    void contextLoads() {
    }

}
