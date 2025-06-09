package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Optional;

@Api(tags = { "Auth" }, description = "Authentication Controls")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    private static final long EXPIRATION_TIME = 300000;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    //TODO los metodos solo pueden ejecutarse si se pasa un token
    //TODO la cookie no se regenera al tener una sesión caducada o inexistente

    @CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Login request")
    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(HttpSession session, @RequestBody User userRequestData) {
        MDC.put("userEmail", userRequestData.getEmail());
        MDC.put("sessionID", session.getId());

        try {
            Optional<User> optionalUser = userService.getUserByEmail(userRequestData.getEmail());
            if (optionalUser.isPresent()) {
                User userLogin = optionalUser.get();

                if (userRequestData.getPassword().equals(userLogin.getPassword())) {

                    session.setAttribute("session", session.getId());

                    Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");

                    Date now = new Date();
                    Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
                    String userJson = new ObjectMapper().writeValueAsString(userRequestData);

                    String token = JWT.create()
                            .withIssuedAt(now)
                            .withExpiresAt(expirationDate)
                            .withClaim("userRequestData", userJson)
                            .sign(algoritmo);

                    logger.info("Login exitoso para: " + userRequestData.getUserName());

                    return ResponseEntity.ok()
                            .header("access-control-expose-headers", "Authorization")
                            .header("Authorization", "Bearer " + token)
                            .body("");
                } else {
                    logger.error("Credenciales incorrectas: " + userRequestData.getUserName());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario no existe");
            }

        } catch (JsonProcessingException e) {
            logger.error("Error al crear token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al iniciar sesión.");
        }
    }

    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @ApiOperation(value = "Check session", notes = "Checks if session cookie is valid")
    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    // Aquí podrías agregar lógica adicional para comprobar en tu sistema
                    // si esa sesión sigue activa, si es necesario
                    return ResponseEntity.ok("Sesión activa");
                }
                else {
                    HttpSession session = request.getSession(true);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión caducada o no válida");
    }
}
