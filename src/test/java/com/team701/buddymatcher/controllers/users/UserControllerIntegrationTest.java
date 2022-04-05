package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.users.User;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "socketio.host=localhost",
        "socketio.port=8085"
})
@AutoConfigureMockMvc
@Sql(scripts = "/user_data.sql")
@Sql(scripts = "/cleanup_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserController userController;

    @MockBean
    UserInterceptor interceptor;

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @BeforeEach
    void initTest() {
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
                .andExpect(jsonPath("$.buddyCount").value(3));
    }

    @Test
    void invalidGetSelf() throws Exception {
        mvc.perform(get("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/users", "null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExistingUser() throws Exception {

        mvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pink Elephant"))
                .andExpect(jsonPath("$.email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$.pairingEnabled").value(false))
                .andExpect(jsonPath("$.buddyCount").value(3));
    }

    @Test
    void invalidGetExistingUser() throws Exception {
        mvc.perform(get("/api/users/{id}", "null"))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/users/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePairingEnabledForSelf() throws Exception {
        mvc.perform(put("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                        .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isOk());

        mvc.perform(put("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 2))
                        .queryParam("pairingEnabled", String.valueOf(false)))
                .andExpect(status().isOk());
    }

    @Test
    void invalidUpdatePairingEnabledForSelf() throws Exception {
        mvc.perform(put("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", 999))
                        .queryParam("pairingEnabled", String.valueOf(true)))
                .andExpect(status().isNotFound());

        mvc.perform(put("/api/users")
                        .sessionAttrs(Collections.singletonMap("UserId", "null"))
                        .queryParam("pairingEnabled", String.valueOf(false)))
                .andExpect(status().isBadRequest());
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
                .andExpect(jsonPath("$[2].pairingEnabled").value(false));
    }

    @Test
    void invalidGetUserBuddy() throws Exception {
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserBuddiesInCourse() throws Exception {
        mvc.perform(get("/api/users/buddy/course/{course_id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Green Dinosaur"))
                .andExpect(jsonPath("$[0].email").value("green.dinosaur@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(false))
                .andExpect(jsonPath("$[1].id").value(4))
                .andExpect(jsonPath("$[1].name").value("Flynn Smith"))
                .andExpect(jsonPath("$[1].email").value("flynn.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(false));
    }

    @Test
    void invalidGetUserBuddiesInCourse() throws Exception {
        mvc.perform(get("/api/users/buddy/course/{course_id}", 999)
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/users/buddy/course/{course_id}", "null")
                        .sessionAttrs(Collections.singletonMap("UserId", 1)))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/users/buddy/course/{course_id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/users/buddy/course/{course_id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBuddy() throws Exception {
        mvc.perform(post("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isOk());
    }

    @Test
    void invalidAddBuddy() throws Exception {
        mvc.perform(post("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(post("/api/users/buddy/{id}", 999)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isNotFound());

        mvc.perform(post("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/users/buddy/{id}", "null")
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBuddy() throws Exception {
        mvc.perform(delete("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isOk());
    }

    @Test
    void reportUser() throws Exception {
        // Test that a POST request for user 2 to report user 1 is OK
        mvc.perform(post("/api/users/report/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2))
                        .queryParam("reportInfo", "This is a test."))
                .andExpect(status().isOk());

        // Test that a POST request for user 2 to report user 1 again is OK
        mvc.perform(post("/api/users/report/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2))
                        .queryParam("reportInfo", "This is another test."))
                .andExpect(status().isOk());

        // Test that a POST request for user 1 to report user 2 as well is OK
        mvc.perform(post("/api/users/report/{id}", 2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                        .queryParam("reportInfo", "This is still a test."))
                .andExpect(status().isOk());

        // Test that a POST request for user 1 to report user 2 as well again is OK
        mvc.perform(post("/api/users/report/{id}", 2)
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                        .queryParam("reportInfo", "This is still another test."))
                .andExpect(status().isOk());
    }

    @Test
    void invalidReportUser() throws Exception {
        // Test that a POST request for user 999 to report user 2 returns 404 Not Found
        mvc.perform(post("/api/users/report/{id}", 2)
                        .sessionAttrs(Collections.singletonMap("UserId", 999))
                        .queryParam("reportInfo", "This is still another test."))
                .andExpect(status().isNotFound());

        // Test that a POST request for user 1 to report user 999 returns 404 Not Found
        mvc.perform(post("/api/users/report/{id}", 999)
                        .sessionAttrs(Collections.singletonMap("UserId", 1))
                        .queryParam("reportInfo", "This is still another test."))
                .andExpect(status().isNotFound());

        // Test that a POST request for user "null" to report user 2 returns 400 Bad Request
        mvc.perform(post("/api/users/report/{id}", 2)
                        .sessionAttrs(Collections.singletonMap("UserId", "null"))
                        .queryParam("reportInfo", "This is still another test."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidDeleteBuddy() throws Exception {
        mvc.perform(delete("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/api/users/buddy/{id}", 999)
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/api/users/buddy/{id}", 4)
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());

        mvc.perform(delete("/api/users/buddy/{id}", "null")
                        .sessionAttrs(Collections.singletonMap("UserId", 3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void blockUser() throws Exception {
        // First test that the first buddy returned from a GET request for user 2 is user 1
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        // Then test that a POST request for user 2 to block user 1 is OK
        mvc.perform(post("/api/users/block/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that the first buddy returned from a GET request for user 2 is now user 3
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));

        // Then test that a POST request for user 2 to block user 3 is OK
        mvc.perform(post("/api/users/block/{id}", 3)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that the first buddy returned from a GET request for user 2 is now user 4
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    void getBlockedUsers() throws Exception {
        // First test that the first buddy returned from a GET request for user 2 is user 1
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        // Then test that a POST request for user 2 to block user 1 is OK
        mvc.perform(post("/api/users/block/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that a GET request for user 2 returns user 1
        mvc.perform(get("/api/users/block")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pink Elephant"))
                .andExpect(jsonPath("$[0].email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(false));

        // Then test that the first buddy returned from a GET request for user 2 is now user 3
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));

        // Then test that a POST request for user 2 to block user 3 is OK
        mvc.perform(post("/api/users/block/{id}", 3)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that a GET request for user 2 returns users 1 and 3
        mvc.perform(get("/api/users/block")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pink Elephant"))
                .andExpect(jsonPath("$[0].email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(false))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Hiruna Smith"))
                .andExpect(jsonPath("$[1].email").value("hiruna.smith@gmail.com"))
                .andExpect(jsonPath("$[1].pairingEnabled").value(false));

        // Then test that the first buddy returned from a GET request for user 2 is now user 4
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    void unblockUser() throws Exception {
        // First test that the first buddy returned from a GET request for user 2 is user 1
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        // Then test that a POST request for user 2 to block user 1 is OK
        mvc.perform(post("/api/users/block/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that a GET request for user 2 returns user 1
        mvc.perform(get("/api/users/block")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pink Elephant"))
                .andExpect(jsonPath("$[0].email").value("pink.elephant@gmail.com"))
                .andExpect(jsonPath("$[0].pairingEnabled").value(false));

        // Then test that a DELETE request for user 2 to unblock user 1 is OK
        mvc.perform(delete("/api/users/unblock/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk());

        // Then test that a GET request for user 2 returns none
        mvc.perform(get("/api/users/block")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldThatShouldNotExist").doesNotExist());
    }
  
    @Test
    void invalidUnblockUser() throws Exception {
        mvc.perform(delete("/api/users/unblock/{id}", 9999)
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isNotFound());
        
        mvc.perform(delete("/api/users/unblock/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", 9999)))
                .andExpect(status().isNotFound());
        
        mvc.perform(delete("/api/users/unblock/{id}", "null")
                        .sessionAttrs(Collections.singletonMap("UserId", 2)))
                .andExpect(status().isBadRequest());
        
        mvc.perform(delete("/api/users/unblock/{id}", 1)
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInvalidBlockedUsers() throws Exception {
        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", 9999)))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/users/buddy")
                        .sessionAttrs(Collections.singletonMap("UserId", "null")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidBlockUser() throws Exception {;
        mvc.perform(post("/api/users/block/{id}", 3)
                        .sessionAttrs(Collections.singletonMap("UserId", 999)))
                .andExpect(status().isNotFound());

        mvc.perform(post("/api/users/block/{id}", 999)
                        .sessionAttrs(Collections.singletonMap("UserId", 4)))
                .andExpect(status().isNotFound());
    }

    @Test
    void fakeLoginUser() throws Exception {
        mvc.perform(get("/api/users/fake/login/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void invalidFakeLoginUser() throws Exception {
        mvc.perform(get("/api/users/fake/login/{id}", 9999))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/users/fake/login/{id}", "null"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void fakeRegisterUser() throws Exception {
        mvc.perform(get("/api/users/fake/register")
                .queryParam("name", "fakeName")
                .queryParam("email", "fake@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    void invalidFakeRegisterUser() throws Exception {
        mvc.perform(get("/api/users/fake/register")
                        .queryParam("name", "fakeName")
                        .queryParam("email", "dumb@email.com"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/users/fake/register")
                        .queryParam("name", "fakeName")
                        .queryParam("email", "dumb@email.com"))
                .andExpect(status().isConflict());

        mvc.perform(get("/api/users/fake/register")
                        .queryParam("name", "fakeName"))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/api/users/fake/register")
                        .queryParam("email", "dumb@email.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void googleLoginUser() throws Exception {
        mvc.perform(get("/api/users/login")
                        .header("id_token", "badToken"))
                .andExpect(status().isBadRequest());

        // TODO add valid google ID token to header for testing successful login
    }
}
