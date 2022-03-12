package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.Pair;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.users.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void retrievingExistingUserReturnsCorrectFields() {
        UUID id = UUID.randomUUID();
        User expected = createExpectedUser(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(expected));

        User user = userService.retrieve(id.toString());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user, expected);
    }

    @Test
    void retrievingNonExistentUserThrowsException() {
        UUID id = UUID.randomUUID();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.retrieve(id.toString()));
    }

    User createExpectedUser(UUID id) {
        return new User()
                .setId(id)
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com")
                .setPair(new Pair());
    }
}
