package com.team701.buddymatcher.controllers.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/user_data.sql")
@Sql(scripts = "/user_cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserController userController;

    @Test
    void getExistingUser() throws Exception {

        mvc.perform(get("/api/users/{id}", 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.name").value("Pink Elephant"))
                .andExpect(jsonPath("$.email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$.buddies.id").value(0L))
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
    void updatePairingEnabledForNonExistingUser() throws Exception {

        mvc.perform(put("/api/users/{id}", new Random().nextLong())
                .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updatePairingEnabledForExistingUser() throws Exception {

        mvc.perform(put("/api/users/{id}", 0L)
                .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.name").value("Pink Elephant"))
                .andExpect(jsonPath("$.email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$.buddies.id").value(0L))
                .andExpect(jsonPath("$.pairingEnabled").value(true))
                .andDo(print());
    }
}
