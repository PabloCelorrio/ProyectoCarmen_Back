package com.example.repository;

import com.example.model.GameData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDataRepository extends JpaRepository<GameData, String> {
}
