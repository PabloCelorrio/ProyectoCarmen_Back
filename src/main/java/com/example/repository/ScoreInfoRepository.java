package com.example.repository;

import com.example.model.ScoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreInfoRepository extends JpaRepository<ScoreInfo, String> {
    List<ScoreInfo> findByUser_UserID(String userId);
}
