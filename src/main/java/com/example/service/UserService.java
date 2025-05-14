package com.example.service;

import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {

        Profile profile = new Profile();
        user.setProfile(profile);

        return userRepository.save(user);
    }

    public boolean createUser(String userName, String pass, String email, Profile profile) {

        User user = new User(userName, pass, email, profile);

        try {
            saveUser(user);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}
