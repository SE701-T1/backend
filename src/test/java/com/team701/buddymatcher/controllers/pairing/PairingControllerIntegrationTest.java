package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.interceptor.UserInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8090"
})
@AutoConfigureMockMvc
@Sql(scripts = "/pairing_data.sql")
@Sql(scripts = "/cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PairingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @MockBean
    UserInterceptor interceptor;

    @Autowired
    PairingController pairingController;


    @BeforeEach
    void initTest() throws Exception {
        mvc = MockMvcBuilders
                .standaloneSetup(pairingController)
                .addInterceptors(interceptor).build();
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);

    }

    @Test
    void requestBuddyMatchFromCourseWithOneBuddy() throws Exception {

        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[3]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[0].name").value("only sevenfifty"))
                .andExpect(jsonPath("$[0].email").value("only.seven@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(true))
                .andDo(print());

    }

    @Test
    void requestBuddyMatchFromCourseWithNoBuddy() throws Exception {

        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[2]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithCourseWithStudentNotPairing() throws Exception {

        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[2]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithBuddy() throws Exception {
        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[5]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithMultipleStudents() throws Exception {

        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[1]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Green Dinosaur"))
                .andExpect(jsonPath("$[0].email").value("green.dinosaur@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(true))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].email").value("hiruna.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(true))
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromMultipleCoursesWithMultipleStudents() throws Exception {
        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[1,3]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Green Dinosaur"))
                .andExpect(jsonPath("$[0].email").value("green.dinosaur@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(true))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].email").value("hiruna.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(true))
                .andExpect(jsonPath("$[2].id").value(6))
                .andExpect(jsonPath("$[2].name").value("only sevenfifty"))
                .andExpect(jsonPath("$[2].email").value("only.seven@gmail.com"))
                .andExpect(jsonPath("$[2].pairingEnabled").value(true))
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCoursesWithSameStudentInBothCourses() throws Exception {
        mvc.perform(post("/api/pairing/matchBuddy")
                        .content("{\"courseIds\":[1,4]}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Green Dinosaur"))
                .andExpect(jsonPath("$[0].email").value("green.dinosaur@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(true))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].email").value("hiruna.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(true))
                .andDo(print());
    }
}
