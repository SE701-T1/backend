package com.team701.buddymatcher.controllers.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void updatePairingEnabledForNonExistingUser() throws Exception {

        mvc.perform(put("/api/users/{id}", new Random().nextLong())
                .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getUserBuddy() throws Exception {

        mvc.perform(get("/api/users/buddy/{id}", 2))
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

        mvc.perform(post("/api/users/buddy/{id}", 3)
                        .queryParam("buddyId", String.valueOf(4)))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(delete("/api/users/buddy/{id}", 3)
                        .queryParam("buddyId", String.valueOf(4)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
