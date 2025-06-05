package com.example.repository;

import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    List<Profile> findByUser_UserID(String userId);
}
