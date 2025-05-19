package com.example.controller;

import com.example.model.GameData;
import com.example.model.ScoreInfo;
import com.example.model.User;
import com.example.service.UserService;
import com.example.repository.GameDataRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    @Autowired
    private GameDataRepository gameDataRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<GameData>> getGamesForUser(@PathVariable String email) {

        List<GameData> games = null;
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);

        if (userOptional.isPresent()) {

            String userId = userOptional.get().getUserId();

            games = gameDataRepository.findByUser_UserId(userId);
            return ResponseEntity.ok(games);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(games);

        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNewGameForUser(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        GameData game = new GameData();
        ScoreInfo scoreInfo = new ScoreInfo();
        game.setGameDataID(game.getGameDataID().toString());
        game.setUser(userOptional.get());;
        game.setScoreInfo(scoreInfo);

        gameDataRepository.save(game);
        return ResponseEntity.ok(game);
    }
}