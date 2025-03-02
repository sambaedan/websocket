package com.websockets.web_socket.service;

import com.websockets.web_socket.model.User;
import com.websockets.web_socket.pojo.model.UserPojo;
import com.websockets.web_socket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl{
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User save(UserPojo userPojo){
        User user = new User();
        user.setUsername(userPojo.getUsername());
        user.setPassword(userPojo.getPassword());
       return userRepository.save(user);
    }

}
