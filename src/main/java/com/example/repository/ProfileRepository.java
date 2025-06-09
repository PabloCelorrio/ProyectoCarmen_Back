package com.example.repository;

import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByUser_UserID(UUID userId);
}
