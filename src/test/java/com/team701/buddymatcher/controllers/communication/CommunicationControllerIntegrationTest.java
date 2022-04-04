package com.team701.buddymatcher.controllers.communication;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8082"
})
@AutoConfigureMockMvc
@Sql(scripts = "/communication_data.sql")
@Sql(scripts = "/cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CommunicationControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    CommunicationController communicationController;

    @MockBean
    UserInterceptor interceptor;

    @BeforeEach
    void initTest() throws Exception {
        mvc = MockMvcBuilders
                .standaloneSetup(communicationController)
                .addInterceptors(interceptor).build();
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void getMessage() throws Exception {
        mvc.perform(get("/api/communication/messages/{id}",2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].senderId").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(2))
                .andExpect(jsonPath("$[0].timestamp").value("1647394176000"))
                .andExpect(jsonPath("$[0].content").value("HARRO"))
                .andExpect(jsonPath("$[0].read").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].senderId").value(2))
                .andExpect(jsonPath("$[1].receiverId").value(1))
                .andExpect(jsonPath("$[1].content").value("Yo HARRO"))
                .andExpect(jsonPath("$[1].timestamp").value("1647394821000"))
                .andExpect(jsonPath("$[1].read").value(false))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].senderId").value(1))
                .andExpect(jsonPath("$[2].receiverId").value(2))
                .andExpect(jsonPath("$[2].content").value("oops"))
                .andExpect(jsonPath("$[2].timestamp").value("1647397776000"))
                .andExpect(jsonPath("$[2].read").value(true))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].senderId").value(2))
                .andExpect(jsonPath("$[3].receiverId").value(1))
                .andExpect(jsonPath("$[3].timestamp").value("1647398421000"))
                .andExpect(jsonPath("$[3].content").value("Yo oops"))
                .andExpect(jsonPath("$[3].read").value(false))
                .andDo(print());
    }

    @Test
    void getChatList() throws Exception {
        mvc.perform(get("/api/communication/chatlist")
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Green Dinosaur"))
                .andExpect(jsonPath("$[0].lastMessage").value("Yo oops"))
                .andExpect(jsonPath("$[0].timestamp").value("1647398421000"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].lastMessage").isEmpty())
                .andExpect(jsonPath("$[1].timestamp").isEmpty())
                .andDo(print());
    }

    @Test
    void deleteMessages() throws Exception {
        // Test that the messages exist before deletion
        mvc.perform(get("/api/communication/messages/{id}",2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].senderId").value(1))
                .andExpect(jsonPath("$[0].receiverId").value(2))
                .andExpect(jsonPath("$[0].timestamp").value("1647394176000"))
                .andExpect(jsonPath("$[0].content").value("HARRO"))
                .andExpect(jsonPath("$[0].read").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].senderId").value(2))
                .andExpect(jsonPath("$[1].receiverId").value(1))
                .andExpect(jsonPath("$[1].content").value("Yo HARRO"))
                .andExpect(jsonPath("$[1].timestamp").value("1647394821000"))
                .andExpect(jsonPath("$[1].read").value(false))
                .andDo(print());

        // Test deleting all messages
        mvc.perform(delete("/api/communication/messages/{id}",2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk());

        // Test that all the messages have been deleted successfully
        mvc.perform(get("/api/communication/messages/{id}",2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // Test when the input user is not found
        mvc.perform(delete("/api/communication/messages/{id}",4)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isNotFound());
    }

    void getMessagesByWords() throws Exception {
        mvc.perform(get("/api/communication/messages/search/{id}",2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                        .param("keywords", "Yo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].senderId").value(2))
                .andExpect(jsonPath("$[0].receiverId").value(1))
                .andExpect(jsonPath("$[0].timestamp").value("1647394821000"))
                .andExpect(jsonPath("$[0].content").value("Yo HARRO"))
                .andExpect(jsonPath("$[0].read").value(false))
                .andExpect(jsonPath("$[1].id").value(4))
                .andExpect(jsonPath("$[1].senderId").value(2))
                .andExpect(jsonPath("$[1].receiverId").value(1))
                .andExpect(jsonPath("$[1].timestamp").value("1647398421000"))
                .andExpect(jsonPath("$[1].content").value("Yo oops"))
                .andExpect(jsonPath("$[1].read").value(false))
                .andDo(print());
    }
}
