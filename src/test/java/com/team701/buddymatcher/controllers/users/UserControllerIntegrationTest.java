package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.interceptor.UserInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8085"
})
@AutoConfigureMockMvc
@Sql(scripts = "/user_data.sql")
@Sql(scripts = "/user_cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserController userController;

    @MockBean
    UserInterceptor interceptor;

    @BeforeEach
    void initTest() throws Exception {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .addInterceptors(interceptor).build();
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void getSelf() throws Exception {

        mvc.perform(get("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pink Elephant"))
                .andExpect(jsonPath("$.email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$.pairingEnabled").value(false))
                .andDo(print());
    }


    @Test
    void getExistingUser() throws Exception {

        mvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pink Elephant"))
                .andExpect(jsonPath("$.email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$.pairingEnabled").value(false))
                .andDo(print());
    }

    @Test
    void getNonExistingUser() throws Exception {

        mvc.perform(get("/api/users/{id}", new Random().nextLong())
                .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updatePairingEnabledForSelf() throws Exception {

        mvc.perform(put("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getUserBuddy() throws Exception {

        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pink Elephant"))
                .andExpect(jsonPath("$[0].email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(false))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].email").value("hiruna.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(false))
                .andExpect(jsonPath("$[2].id").value(4))
                .andExpect(jsonPath("$[2].name").value("Flynn Smith"))
                .andExpect(jsonPath("$[2].email").value("flynn.smith@gmail.com"))
                .andExpect(jsonPath("$[2].pairingEnabled").value(false))
                .andDo(print());
    }

    @Test
    void createAndDeleteUserBuddy() throws Exception {

        mvc.perform(post("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(delete("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
