package com.team701.buddymatcher.controllers.timetable;

import com.team701.buddymatcher.interceptor.UserInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8089"
})
@AutoConfigureMockMvc
@Sql(scripts = "/timetable_data.sql")
@Sql(scripts = "/cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TimetableControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    TimetableController timetableController;

    @MockBean
    UserInterceptor interceptor;

    @BeforeEach
    void initTest() throws Exception {
        mvc = MockMvcBuilders
                .standaloneSetup(timetableController)
                .addInterceptors(interceptor).build();
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void getUserCourses() throws Exception {
        mvc.perform(get("/api/timetable/users/courses")
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].name").value("SOFTENG 701"))
                .andExpect(jsonPath("$[0].semester").value("Semester 1 2022"))
                .andExpect(jsonPath("$[0].studentCount").value(10))
                .andExpect(jsonPath("$[0].updatedTime").value("1647394176000"))
                .andDo(print());
    }

    @Test
    void getCourses() throws Exception {
        mvc.perform(get("/api/timetable/users/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].name").value("SOFTENG 701"))
                .andExpect(jsonPath("$[0].semester").value("Semester 1 2022"))
                .andExpect(jsonPath("$[0].studentCount").value(10))
                .andExpect(jsonPath("$[0].updatedTime").value("1647394176000"))
                .andDo(print());
    }

    @Test
    void getCourse() throws Exception {
        mvc.perform(get("/api/timetable/course/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.name").value("SOFTENG 701"))
                .andExpect(jsonPath("$.semester").value("Semester 1 2022"))
                .andExpect(jsonPath("$.studentCount").value(10))
                .andExpect(jsonPath("$.updatedTime").value("1647394176000"))
                .andDo(print());
    }

    @Test
    // TODO find a way to access local resource for test as file URI doesn't work
    void uploadTimetable() {
//        File f = new File("src/test/resources/UoACal.ics");
//        mvc.perform(post("/api/timetable/users/upload/{id}", 1)
//                .content(f.toURI().toString()))
//                .andExpect(status().isOk());
    }

    @Test
    void uploadTimetableFile() throws Exception {
        // Take the UoACal.ics file and turn it into a MockMultipartFile
        Path path = Paths.get("src/test/resources/UoACal.ics");
        String name = "file";
        String originalFileName = "UoACal.ics";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MockMultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        mvc.perform(multipart("/api/timetable/users/upload/file").file(file)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk());
    }

    /**
     * Test un-enrolling users from courses using HTTP DELETE request and timetable_data.sql resource
     */
    @Test
    void deleteCourse() throws Exception {
        // Will return 200 OK when user 1 un-enrolls from course 1
        mvc.perform(delete("/api/timetable/course/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk());

        // Will return 404 Not Found because user 1 is no longer enrolled in course 1
        mvc.perform(delete("/api/timetable/course/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isNotFound());

        // Will return 200 OK when user 2 un-enrolls from course 1
        mvc.perform(delete("/api/timetable/course/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Will return 404 Not Found because user 2 is no longer enrolled in course 1
        mvc.perform(delete("/api/timetable/course/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isNotFound());
    }
}
