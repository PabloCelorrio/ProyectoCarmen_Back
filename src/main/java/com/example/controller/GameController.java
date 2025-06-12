package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.model.*;
import com.example.service.GameDataService;
import com.example.service.ScoreInfoService;
import com.example.service.UserService;
import com.example.repository.GameDataRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(tags = { "Game" }, description = "In-and-Out Game Controls")
@RestController
@RequestMapping("/api/games")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameDataRepository gameDataRepository;

    @Autowired
    private GameDataService gameDataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreInfoService scoreInfoService;

    @CrossOrigin(
            origins = {
                    "https://carmen-sandiego.loca.lt",
                    "http://localhost:3000"
            },
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.GET}
    )
    @GetMapping("/saved-games")
    public ResponseEntity<List<GameData>> getGamesForUser(@RequestHeader("Authorization") String authHeader) {

        List<GameData> games = null;

        try {

            String token = authHeader.replace("Bearer ", "");
            Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");
            DecodedJWT jwt = JWT.require(algoritmo).build().verify(token);

            String userJson = jwt.getClaim("userRequestData").asString();
            ObjectMapper mapper = new ObjectMapper();

            JwtUser jwtUser = mapper.readValue(userJson, JwtUser.class);

            Optional<User> userOptional = userService.getUserByEmail(jwtUser.getEmail());

            if (userOptional.isPresent()) {

                User user = userOptional.get();

                games = gameDataRepository.findByUser_UserID(user.getUserId());
                return ResponseEntity.ok(games);

            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(games);

            }
        }catch (Exception e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(games);

        }
    }

    @CrossOrigin(
            origins = {
                    "https://carmen-sandiego.loca.lt",
                    "http://localhost:3000"
            },
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.POST}
    )
    @PostMapping("/saved-games")
    public ResponseEntity<?> createNewGameForUser(@RequestHeader("Authorization") String authHeader) {
        try {

            String token = authHeader.replace("Bearer ", "");
            Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");
            DecodedJWT jwt = JWT.require(algoritmo).build().verify(token);

            String userJson = jwt.getClaim("userRequestData").asString();
            ObjectMapper mapper = new ObjectMapper();

            JwtUser jwtUser = mapper.readValue(userJson, JwtUser.class);

            Optional<User> userOptional = userService.getUserByEmail(jwtUser.getEmail());

            if (!userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            GameData game = new GameData();
            //game.setGameDataID(game.getGameDataID());
            game.setUser(userOptional.get());

            gameDataRepository.save(game);
            return ResponseEntity.ok(game);

        } catch (Exception e) {

            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido crear la partida" + e.getMessage());

        }
    }

    @CrossOrigin(
            origins = {
                    "https://carmen-sandiego.loca.lt",
                    "http://localhost:3000"
            },
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.POST}
    )

    @PostMapping("/score-save")
    public ResponseEntity<?> createScore(@RequestBody ScoreInfoResponse request) {

        Optional<GameData> gameDataOptional = gameDataService.getSaveStateById(request.getGameDataID());

        if (gameDataOptional.isPresent()) {

            GameData savedState = gameDataOptional.get();

            ScoreInfo newScore = new ScoreInfo(savedState.getOccupation(), request.isGameWon(), request.getScore(), savedState);
            scoreInfoService.saveScore(newScore);

            if (request.isGameWon()) {

                savedState.setWins(savedState.getWins() + 1);

                switch (savedState.getWins()) {
                    case 4:
                        savedState.setOccupation("Official");
                        break;
                    case 10:
                        savedState.setOccupation("Inspector");
                        break;
                    case 100:
                        savedState.setOccupation("Director");
                        break;
                }

            }else {

                savedState.setLoses(savedState.getLoses() + 1);

            }

            gameDataService.saveState(savedState);

            return ResponseEntity.ok(newScore);

        }
        else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la partida guardada");

        }
    }
}