package com.team701.buddymatcher.services.users.impl;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.users.UserRepository;
import com.team701.buddymatcher.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User retrieveById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User updatePairingEnabled(Long id, Boolean pairingEnabled) {
        int rowsUpdated = userRepository.updatePairingEnabled(id, pairingEnabled);
        if(rowsUpdated == 0) {
            throw new NoSuchElementException();
        }
        return retrieveById(id);
    }

    @Override
    public User retrieveByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void addUser(String name, String email) {
        userRepository.createUser(name, email);
    }

    @Override
    public List<User> retrieveBuddiesByUserId(Long userId) {
        return userRepository.findBuddies(userId);
    }

    @Override
    public Integer retrieveBuddyCountByUserId(Long userId) {
        return userRepository.countBuddies(userId);
    }

    @Override
    public void addBuddy(Long currentUserId, Long buddyUserId) throws NoSuchElementException  {
        Long[] buddies = this.orderBuddyId(currentUserId, buddyUserId);

        userRepository.createBuddy(buddies[0], buddies[1]);
    }

    @Override
    public void deleteBuddy(Long currentUserId, Long buddyUserId)  throws NoSuchElementException {
        Long[] buddies = this.orderBuddyId(currentUserId, buddyUserId);

        userRepository.deleteBuddy(buddies[0], buddies[1]);
    }

    /**
     * OrderBuddyId in terms of user0Id < user1Id. user0Id != user1Id based on
     * database structure. This is to ensure we only have one relation per buddy
     * pair
     * @param user0Id
     * @param user1Id
     * @return Long[ user0Id, user1Id ] in order
     * @throws Exception
     */
    private Long[] orderBuddyId(Long user0Id, Long user1Id) throws NoSuchElementException {
        if (user0Id == user1Id) {
            throw new NoSuchElementException("Cannot be a buddy of yourself");
        }
        if (user0Id > user1Id) {
            return new Long[] {user1Id, user0Id};
        }
        return new Long[] {user0Id, user1Id};
    }
}
