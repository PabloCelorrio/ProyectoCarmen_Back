package com.example.service;

import com.example.model.GameData;
import com.example.model.User;
import com.example.repository.GameDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameDataService {

    public static final int MAX_SAVEDGAMES = 3;

    @Autowired
    private GameDataRepository gameDataRepository;

    public List<GameData> getAllSaveStates() {
        return gameDataRepository.findAll();
    }

    public Optional<GameData> getSaveStateById(String gameDataId) {
        return gameDataRepository.findById(gameDataId);
    }

    public boolean saveState(GameData gameData) {

        List<GameData> savedGames = gameDataRepository.findAll();

        if(savedGames.stream().count() < MAX_SAVEDGAMES || gameData.getGameDataID() == null) {
            gameDataRepository.save(gameData);
            return true;
        }
        else if(savedGames.stream().count() == MAX_SAVEDGAMES && gameDataRepository.existsById(gameData.getGameDataID())) {
            gameDataRepository.save(gameData);
            return true;
        }
        else {
            return false;
        }

    }
}
