package com.example.service;

import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile saveProfile(Profile profile) {

        return profileRepository.save(profile);
    }

}
