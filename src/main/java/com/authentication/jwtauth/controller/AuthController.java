package com.authentication.jwtauth.controller;

import com.authentication.jwtauth.customResponse.CustomResponse;
import com.authentication.jwtauth.model.UserModel;
import com.authentication.jwtauth.service.UserServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AuthController {

    private final UserServiceImplementation userService;
    private final CustomResponse customResponse;


    public AuthController(UserServiceImplementation userService, CustomResponse customResponse) {
        this.userService = userService;
        this.customResponse = customResponse;
    }

    //API ENDPOINTS
    @GetMapping
    public List<String> welcomeMessage() {
        return userService.welcomeMessage();
    }

    @PostMapping(path = "/create-user")
    public ResponseEntity<CustomResponse> createUser(@RequestBody UserModel userModel) {
        return userService.register(userModel);
    }

    @PostMapping(path = "/login-user")
    public ResponseEntity<CustomResponse> loginUser(@RequestBody UserModel userModel) {
        return userService.login(userModel);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<CustomResponse> getAllUsers() {
        return userService.userList();
    }


}
