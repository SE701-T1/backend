package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8087"
})
@AutoConfigureMockMvc
public class ValidateControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ValidateController validateController;

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Test
    void validateValidToken() throws Exception {
        String token = createValidToken();

        mvc.perform(get("/api/validate")
                .header("token", token))
                .andExpect(status().isNoContent());
    }

    @Test
    void validateInvalidToken() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        mvc.perform(get("/api/validate")
                .header("token", token))
                .andExpect(status().isUnauthorized());
    }

    private String createValidToken() {
        User user = new User()
                .setId(new Random().nextLong())
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com");

        return jwtTokenUtil.generateToken(user);
    }
}
