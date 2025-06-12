package com.example.service;

import com.example.model.ScoreInfo;
import com.example.repository.ScoreInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreInfoService {

    @Autowired
    private ScoreInfoRepository scoreInfoRepository;

    public ScoreInfo saveScore(ScoreInfo scoreInfo) {

        return scoreInfoRepository.save(scoreInfo);

    }
}
