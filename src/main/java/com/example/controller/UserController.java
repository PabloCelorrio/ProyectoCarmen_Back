package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.config.SessionConfig;
import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//@Slf4j
@Api(tags = { "User" },  description = "User Controls")
@RestController
@RequestMapping("/api")
@SessionAttributes("sessionID")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    User usuario = new User("admin", "admin", "admin@admin.es");
    private static final long EXPIRATION_TIME = 300000;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Login request", notes="Login request, which returns a valid token in response headers.")
    @PostMapping(path="/login", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity login(HttpSession session, @RequestBody User usuarioLogin) {

        MDC.put("userId", usuario.getUserId().toString());
        MDC.put("sessionID", session.getId());

        try {

            if (usuarioLogin.getUser().equals(usuario.getUser()) && usuarioLogin.getPass().equals(usuario.getPass())) {

                Algorithm algoritmo = Algorithm.HMAC256("Prueba");

                Date now = new Date();
                Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

                String userJson = new ObjectMapper().writeValueAsString(usuario);

                String token = JWT.create()
                        .withIssuedAt(now)
                        .withExpiresAt(expirationDate)
                        .withClaim("user", userJson)
                        .sign(algoritmo);

                ResponseEntity response = ResponseEntity.ok().header("access-control-expose-headers", "Authorization")
                                                             .header("Authorization", "Bearer " + token)
                                                             .body("");

                logger.debug("/login executed successfully");
                logger.info(String.format("User %s logged in correctly", usuarioLogin.getUser()));

                return response;

            } else {

                logger.error(String.format("Incorrect credentials for user %s", usuarioLogin.getUser()));


                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");

            }
        }catch (JsonProcessingException exception){

            logger.error(String.format("There was a problem creating the token: %s", exception.getMessage()));


            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hubo un problema en el inicio de sesión.");

        }

    }

    @PostMapping(path= "/pass-change", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Password change request", notes="Password change request, which returns a text sequence in response headers. Pass is the new password of the user")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization",
            value = "JWT Token",
            required = true,
            dataType = "string",
            paramType = "header") })
    public ResponseEntity passwordChange(HttpSession session, @RequestHeader("Authorization") String token, @RequestBody User usuarioNewPass) {

        MDC.put("sessionID", session.getId());

        try {

            Algorithm algoritmo = Algorithm.HMAC256("Prueba");

            String[] tokenParts = token.split(" ");

            DecodedJWT decodedToken = JWT.decode(tokenParts[1]);
            Date expirationDate = decodedToken.getExpiresAt();

            String userClaim = decodedToken.getClaim("user").asString();
            User usuarioOldPass = new ObjectMapper().readValue(userClaim, User.class);

            MDC.put("userId", usuarioOldPass.getUserId().toString());

            if(usuarioOldPass.getUser().equals(usuario.getUser()) && usuarioOldPass.getPass().equals(usuario.getPass()) && expirationDate.after(new Date())) {

                usuario = usuarioNewPass;
                logger.debug("/pass-change executed successfully");
                logger.info("User changed the password correctly");
                return ResponseEntity.ok().body("Contraseña cambiada");

            }else{

                if(expirationDate.after(new Date())) {

                    logger.error(String.format("Incorrect credentials for user %s", usuarioOldPass.getUser()));
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");

                }else{

                    logger.error("The token has expired");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expirado");

                }
            }

        }catch(Exception e){

            logger.error("Invalid token");
            logger.debug("Error: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");

        }

    }

}
