package com.steven.Messaging.Club.Services;


import com.steven.Messaging.Club.Dtos.LoginAuthenticationDto;
import com.steven.Messaging.Club.Dtos.RegisterDto;
import com.steven.Messaging.Club.Entities.UserEntity;
import com.steven.Messaging.Club.Repositories.UserIRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AuthenticationService {


    private final UserIRepository userIRepository;
    private UserIRepository userRepository;

    private  PasswordEncoder passwordEncoder;

    private  AuthenticationManager authenticationManager;

    public AuthenticationService(UserIRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserIRepository userIRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userIRepository = userIRepository;
    }


    public UserEntity signup(RegisterDto registerDto) {

            UserEntity   user = new UserEntity();
            user.setUsername(registerDto.getUsername());
            user.setEmail(registerDto.getEmail());
            user.setPhone(registerDto.getPhone());
            user.setCountry(registerDto.getCountry());
            user.setGender(registerDto.getGender());
            user.setAddress(registerDto.getAddress());
            user.setDob(registerDto.getDob());
            user.setStatus(registerDto.getStatus());
            user.setFile(registerDto.getImageName());
            return userRepository.save(user);
    }

    public Optional<UserEntity> updateStatus() {
        return userRepository.updateStatusBeforeConnecting("unconnected");
    }



    public UserEntity authenticate(LoginAuthenticationDto input) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public UserEntity uploadProfile(int id, UserEntity user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public UserEntity updateStatusById(int id, UserEntity user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return  user;
    }

//    public List<Object> getCurrentConversationsByPhones(String sender, String receptor, int id) {
//        return userIRepository.findLastConversationsByPhone(sender, receptor, id);
//    }
}
