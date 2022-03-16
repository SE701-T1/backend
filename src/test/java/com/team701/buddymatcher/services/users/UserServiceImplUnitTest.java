package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.Buddies;
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
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void retrievingExistingUserReturnsCorrectFields() {
        Long id = new Random().nextLong();
        User expected = createExpectedUser(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(expected));

        User user = userService.retrieveById(id);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user, expected);
    }

    @Test
    void retrievingNonExistentUserThrowsException() {
        Long id = new Random().nextLong();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.retrieveById(id));
    }

    @Test
    void updatingExistingUsersPairingEnabled() {
        Long id = new Random().nextLong();
        User expected = createExpectedUser(id);
        Mockito.when(userRepository.updatePairingEnabled(id, true)).thenReturn(1);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(expected));

        User user = userService.updatePairingEnabled(id, true);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getPairingEnabled(), expected.getPairingEnabled());
    }

    @Test
    void updatingNonExistentUsersPairingEnabled() {
        Long id = new Random().nextLong();
        Mockito.when(userRepository.updatePairingEnabled(id, true)).thenReturn(0);

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.updatePairingEnabled(id, true));
    }

    User createExpectedUser(Long id) {
        return new User()
                .setId(id)
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com")
                .setBuddies(new Buddies())
                .setPairingEnabled(false);
    }
}
