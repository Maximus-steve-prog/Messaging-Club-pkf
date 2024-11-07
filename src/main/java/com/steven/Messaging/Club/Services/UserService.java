package com.steven.Messaging.Club.Services;

import com.steven.Messaging.Club.Entities.UserEntity;
import com.steven.Messaging.Club.Repositories.UserIRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserIRepository userRepository;

    public UserService(UserIRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> allUsers() {
        List<UserEntity> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}
