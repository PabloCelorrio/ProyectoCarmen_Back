package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.model.JwtUser;
import com.example.model.Profile;
import com.example.model.User;
import com.example.service.ProfileService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Api(tags = { "User" }, description = "User CRUD")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    @GetMapping
    @ApiOperation(value = "Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @CrossOrigin(
            origins = {
                    "https://carmen-sandiego.loca.lt",
                    "http://localhost:3000"
            },
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.GET}
    )

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by ID")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static final long EXPIRATION_TIME = 300000;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /*@CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Create request")
    @PostMapping(path="/user/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(HttpSession session, @RequestBody User userData) {
        if (!userService.getUserByEmail(userData.getEmail()).isPresent()) {
            try {
                userService.createUser(userData.getUserName(), userData.getPassword(), userData.getEmail(), null);

                Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");
                Date now = new Date();
                Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
                String userJson = new ObjectMapper().writeValueAsString(userData);

                String token = JWT.create()
                        .withIssuedAt(now)
                        .withExpiresAt(expirationDate)
                        .withClaim("user", userJson)
                        .sign(algoritmo);

                return ResponseEntity.ok()
                        .header("access-control-expose-headers", "Authorization")
                        .header("Authorization", "Bearer " + token)
                        .body("");

            } catch (Exception e) {
                logger.error(String.format("Error while creating user %s: %s", userData.getUserName(), e.getMessage()));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la creación del usuario.");
            }
        } else {
            logger.error("User already exists: " + userData.getUserName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El usuario ya existe.");
        }
    }*/

    @CrossOrigin(
            origins = {
                    "https://carmen-sandiego.loca.lt",
                    "http://localhost:3000"
            },
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.POST}
    )

    @ApiOperation(value = "Create request")
    @PostMapping(
            path = "/user/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createUser(
            HttpSession session,
            @RequestParam("userName") String userName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        if (!userService.getUserByEmail(email).isPresent()) {
            try {
                // Puedes guardar la imagen si lo deseas
                byte[] imageBytes = null;
                if (imageFile != null && !imageFile.isEmpty()) {
                    imageBytes = imageFile.getBytes(); // o guárdalo en disco, etc.
                }

                Profile profile = new Profile();
                profile.setProfileImg(imageBytes);

                profileService.saveProfile(profile);

                userService.createUser(userName, password, email, profile);

                Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");
                Date now = new Date();
                Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

                JwtUser jwtUser = new JwtUser(email, password);
                String userJson = new ObjectMapper().writeValueAsString(jwtUser);

                String token = JWT.create()
                        .withIssuedAt(now)
                        .withExpiresAt(expirationDate)
                        .withClaim("user", userJson)
                        .sign(algoritmo);

                return ResponseEntity.ok()
                        .header("access-control-expose-headers", "Authorization")
                        .header("Authorization", "Bearer " + token)
                        .body("");

            } catch (Exception e) {
                logger.error(String.format("Error while creating user %s: %s", userName, e.getMessage()));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error en la creación del usuario.");
            }
        } else {
            logger.error("User already exists: " + userName);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("El usuario ya existe.");
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

    @PostMapping(path= "/pass-change", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Password change request", notes="Password change request, which returns a text sequence in response headers. Pass is the new password of the user")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization",
            value = "JWT Token",
            required = true,
            dataType = "string",
            paramType = "header") })
    public ResponseEntity passwordChange(HttpSession session, @RequestHeader("Authorization") String token, @RequestBody User usuarioNewPass) {

        MDC.put("sessionID", session.getId());

        try {

            Algorithm algoritmo = Algorithm.HMAC256("CarmenSandiego");

            String[] tokenParts = token.split(" ");

            DecodedJWT decodedToken = JWT.decode(tokenParts[1]);
            Date expirationDate = decodedToken.getExpiresAt();

            String userClaim = decodedToken.getClaim("user").asString();
            JwtUser usuarioOldPass = new ObjectMapper().readValue(userClaim, JwtUser.class);

            MDC.put("userId", usuarioOldPass.getEmail());

            if(usuarioOldPass.getEmail().equals(usuarioNewPass.getEmail()) && usuarioOldPass.getPassword().equals(usuarioNewPass.getPassword()) && expirationDate.after(new Date())) {

                userService.saveUser(usuarioNewPass);

                logger.debug("/pass-change executed successfully");
                logger.info("User changed the password correctly");
                return ResponseEntity.ok().body("Contraseña cambiada");

            }else{

                if(expirationDate.after(new Date())) {

                    logger.error(String.format("Incorrect credentials for user %s", usuarioOldPass.getEmail()));
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
