package com.example.repository;

import com.example.model.GameData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameDataRepository extends JpaRepository<GameData, String> {
    List<GameData> findByUser_UserID(String userId);
}
