package com.team701.buddymatcher.controllers.pairing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

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
@Sql(scripts = "/pairing_cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PairingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    PairingController pairingController;

    @Test
    void requestBuddyMatchFromCourseWithOneBuddy() throws Exception {

        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[3]}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(6))
                .andExpect(jsonPath("$[0].name").value("only sevenfifty"))
                .andExpect(jsonPath("$[0].email").value("only.seven@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(true))
                .andDo(print());

    }

    @Test
    void requestBuddyMatchFromCourseWithNoBuddy() throws Exception {

        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[2]}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithCourseWithStudentNotPairing() throws Exception {

        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[2]}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithBuddy() throws Exception {
        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[5]}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void requestBuddyMatchFromCourseWithMultipleStudents() throws Exception {

        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[1]}")
                        .contentType(MediaType.APPLICATION_JSON))
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
        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[1,3]}")
                        .contentType(MediaType.APPLICATION_JSON))
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
        mvc.perform(get("/api/pairing/matchBuddy/{id}",4)
                        .content("{\"courseIds\":[1,4]}")
                        .contentType(MediaType.APPLICATION_JSON))
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
    void addValidBuddy() throws Exception {

        mvc.perform(post("/api/pairing/addBuddy/")
                        .content("{\"userId\":3, \"buddyId\":4}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Disabled
    @Test
    void addValidBuddyButInvalidUser() throws Exception {
        //ToDo: need to refactor to return better status code
        mvc.perform(post("/api/pairing/addBuddy/")
                        .content("{\"userId\":7, \"buddyId\":3}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Disabled
    @Test
    void addAlreadyAddedBuddy() throws Exception {
        //ToDo: need to refactor to return better status code
        mvc.perform(post("/api/pairing/addBuddy/")
                        .content("{\"userId\":4, \"buddyId\":5}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Disabled
    @Test
    void addInvalidBuddy() throws Exception {
        //ToDo: need to refactor to return better status code
        mvc.perform(post("/api/pairing/addBuddy/")
                        .content("{\"userId\":4, \"buddyId\":7}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Disabled
    @Test
    void addBuddyWhoDoesNotHavePairingEnabled() throws Exception {
        //ToDo: need to refactor to return better status code
        mvc.perform(post("/api/pairing/addBuddy/")
                        .content("{\"userId\":4, \"buddyId\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void removeBuddyValid() throws Exception {

        mvc.perform(delete("/api/pairing/removeBuddy/")
                        .content("{\"userId\":4, \"buddyId\":5}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void removeBuddyRecordThatDoesNotExist() throws Exception {
        //ToDo: need to refactor to return better status code
        mvc.perform(delete("/api/pairing/removeBuddy/")
                        .content("{\"userId\":4, \"buddyId\":3}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
