package com.team701.buddymatcher.controllers.timetable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/timetable_data.sql")
@Sql(scripts = "/timetable_cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(properties = {
        "socketio.host=localhost",
        "socketio.port=8081"
})
public class TimetableControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    TimetableController timetableController;

    @Test
    void getUserCourses() throws Exception {

        mvc.perform(get("/api/timetable/users/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].name").value("SOFTENG 701"))
                .andExpect(jsonPath("$[0].semester").value("Semester 1 2022"))
                .andExpect(jsonPath("$[0].studentCount").value(10))
                .andExpect(jsonPath("$[0].updatedTime").value("2022-03-16T01:29:36.000+00:00"))
                .andDo(print());
    }

    @Test
    void getCourse() throws Exception {

        mvc.perform(get("/api/timetable/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.name").value("SOFTENG 701"))
                .andExpect(jsonPath("$.semester").value("Semester 1 2022"))
                .andExpect(jsonPath("$.studentCount").value(10))
                .andExpect(jsonPath("$.updatedTime").value("2022-03-16T01:29:36.000+00:00"))
                .andDo(print());
    }
}

