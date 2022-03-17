package com.team701.buddymatcher.controllers.communication;

import com.team701.buddymatcher.controllers.users.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8082"
})
@AutoConfigureMockMvc
@Sql(scripts = "/communication_data.sql")
@Sql(scripts = "/communication_cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CommunicationControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserController userController;

    @Test
    void getMessage() throws Exception {
        mvc.perform(get("/api/communication/messages/{id}", 1)
                .queryParam("buddyId", String.valueOf(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].senderId").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(2))
                .andExpect(jsonPath("$[0].timestamp").value("2022-03-16T01:29:36.000+00:00"))
                .andExpect(jsonPath("$[0].content").value("HARRO"))
                .andExpect(jsonPath("$[0].read").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].senderId").value(2))
                .andExpect(jsonPath("$[1].receiverId").value(1))
                .andExpect(jsonPath("$[1].content").value("Yo HARRO"))
                .andExpect(jsonPath("$[1].timestamp").value("2022-03-16T01:40:21.000+00:00"))
                .andExpect(jsonPath("$[1].read").value(false))
                .andDo(print());
    }
}
