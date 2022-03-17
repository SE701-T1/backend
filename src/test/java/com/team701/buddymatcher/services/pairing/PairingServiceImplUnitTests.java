package com.team701.buddymatcher.services.pairing;

import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.repositories.users.BuddiesRepository;
import com.team701.buddymatcher.services.pairing.impl.PairingServiceImpl;
import com.team701.buddymatcher.services.users.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class PairingServiceImplUnitTests {

    @Mock
    private BuddiesRepository buddiesRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PairingServiceImpl pairingService;

    @Test
    void correctlyAddBuddy(){

        Long user_1_Id = new Random().nextLong();
        Long buddies_1_Id = new Random().nextLong();
        List buddies_1_list = new ArrayList();
        Buddies buddies_1 = new Buddies().setId(buddies_1_Id).setUsers(buddies_1_list);
        User user_1 = createExpectedUser_1(user_1_Id, buddies_1);

        Long user_2_Id = new Random().nextLong();
        Long buddies_2_Id = new Random().nextLong();
        List buddies_2_list = new ArrayList();
        Buddies buddies_2 = new Buddies().setId(buddies_2_Id).setUsers(buddies_2_list);
        User user_2 = createExpectedUser_2(user_2_Id,buddies_2);

        Mockito.when(userService.retrieveById(user_1_Id)).thenReturn(user_1);
        Mockito.when(userService.retrieveById(user_2_Id)).thenReturn(user_2);

        Mockito.when(buddiesRepository.findById(user_1.getBuddies().getId())).thenReturn(Optional.ofNullable(buddies_1));
        Mockito.when(buddiesRepository.findById(user_2.getBuddies().getId())).thenReturn(Optional.ofNullable(buddies_2));

        pairingService.addBuddy(user_1_Id, user_2_Id);

        Assertions.assertEquals(buddies_1.getUsers().size(),1);
        Assertions.assertEquals(buddies_1.getUsers().get(0),user_2);
        Assertions.assertEquals(buddies_2.getUsers().size(),1);
        Assertions.assertEquals(buddies_2.getUsers().get(0),user_1);
    }

    @Test
    void duplicationAddBuddy(){
        Long user_1_Id = new Random().nextLong();
        Long buddies_1_Id = new Random().nextLong();
        List buddies_1_list = new ArrayList();
        Buddies buddies_1 = new Buddies().setId(buddies_1_Id).setUsers(buddies_1_list);
        User user_1 = createExpectedUser_1(user_1_Id, buddies_1);

        Long user_2_Id = new Random().nextLong();
        Long buddies_2_Id = new Random().nextLong();
        List buddies_2_list = new ArrayList();
        Buddies buddies_2 = new Buddies().setId(buddies_2_Id).setUsers(buddies_2_list);
        User user_2 = createExpectedUser_2(user_2_Id,buddies_2);

        buddies_1_list.add(user_2);
        buddies_2_list.add(user_1);

        Mockito.when(userService.retrieveById(user_1_Id)).thenReturn(user_1);
        Mockito.when(userService.retrieveById(user_2_Id)).thenReturn(user_2);

        Mockito.when(buddiesRepository.findById(user_1.getBuddies().getId())).thenReturn(Optional.ofNullable(buddies_1));
        Mockito.when(buddiesRepository.findById(user_2.getBuddies().getId())).thenReturn(Optional.ofNullable(buddies_2));

        pairingService.addBuddy(user_1_Id, user_2_Id);

        Assertions.assertEquals(buddies_1.getUsers().size(),1);
        Assertions.assertEquals(buddies_1.getUsers().get(0),user_2);
        Assertions.assertEquals(buddies_2.getUsers().size(),1);
        Assertions.assertEquals(buddies_2.getUsers().get(0),user_1);
    }

    User createExpectedUser_1(Long id, Buddies buddies) {
        return new User()
                .setId(id)
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com")
                .setBuddies(buddies)
                .setPairingEnabled(false);
    }
    User createExpectedUser_2(Long id, Buddies buddies) {
        return new User()
                .setId(id)
                .setName("Blue Kora")
                .setEmail("bluekoraaa@gmail.com")
                .setBuddies(buddies)
                .setPairingEnabled(false);
    }
}
