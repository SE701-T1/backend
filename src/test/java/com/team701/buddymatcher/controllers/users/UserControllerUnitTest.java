package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.interceptor.UserInterceptor;
import com.team701.buddymatcher.services.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private UserService userService;



    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;

    @MockBean
    UserInterceptor interceptor;

    @Test
    void retrievingExistingUserReturnsCorrectResponseEntity() {
        Long id = new Random().nextLong();

        User mockedUser = createMockUser(id);
        when(userService.retrieveById(id)).thenReturn(mockedUser);

        UserDTO mockedUserDTO = createMockedUserDTO(mockedUser);
        when(modelMapper.map(mockedUser, UserDTO.class)).thenReturn(mockedUserDTO);

        ResponseEntity<UserDTO> response = userController.retrieveUserById(id);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody(), mockedUserDTO);
    }

    @Test
    void retrievingNonExistentUserThrowsException() {
        Long id = new Random().nextLong();

        when(userService.retrieveById(id)).thenThrow(new NoSuchElementException());

        Assertions.assertThrows(ResponseStatusException.class, () -> userController.retrieveUserById(id));

    }

    User createMockUser(Long id) {
        return new User()
                .setId(id)
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com");
    }

    UserDTO createMockedUserDTO(User mockedUser) {
        return new UserDTO()
                .setId(mockedUser.getId())
                .setName(mockedUser.getName())
                .setEmail(mockedUser.getEmail())
                .setBuddyCount(2L);
    }
}
