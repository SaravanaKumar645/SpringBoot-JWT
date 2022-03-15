package com.authentication.jwtauth.service;

import com.authentication.jwtauth.customResponse.CustomResponse;
import com.authentication.jwtauth.model.UserModel;
import com.authentication.jwtauth.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class UserServiceImplementation {
    private final UserRepository userRepository;
    private final CustomResponse customResponse;
    private final JWT_Utils jwtUtils;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, CustomResponse customResponse, JWT_Utils jwtUtils) {
        this.userRepository = userRepository;
        this.customResponse = customResponse;
        this.jwtUtils = jwtUtils;
    }

    public List<String> welcomeMessage() {
        log.info("Application Started");
        return List.of(
                "Application is live and running",
                "status:200 "
        );
    }

    public ResponseEntity<CustomResponse> userList() {
        List<Object> users = Collections.singletonList(userRepository.findAll());
        customResponse.setSuccess(true);
        customResponse.setMessage("Logged all users !,");
        customResponse.setDetails(users);

        return ResponseEntity.ok(customResponse);
    }

    public ResponseEntity<CustomResponse> register(UserModel userModel) {
        Optional<UserModel> user = userRepository.findByEmail(userModel.getEmail());

        if (user.isPresent()) {
            customResponse.setSuccess(false);
            customResponse.setMessage("User Already Exists !,");
            customResponse.setDetails(List.of(user));
            return ResponseEntity.status(403).body(customResponse);
        }
        String password = BCrypt.hashpw(userModel.getPassword(), BCrypt.gensalt());
        userModel.setPassword(password);
        userRepository.save(userModel);
        customResponse.setSuccess(true);
        customResponse.setMessage("Successfully created User !,");
        customResponse.setDetails(List.of(userModel));
        return ResponseEntity.ok(customResponse);
    }

    public ResponseEntity<CustomResponse> login(UserModel userModel) {
        Optional<UserModel> user = userRepository.findByEmail(userModel.getEmail());
        if (user.isPresent()) {
            System.out.println(user);
            boolean isPasswordMatch = BCrypt.checkpw(userModel.getPassword(), user.get().getPassword());
            if (isPasswordMatch) {

                String token = jwtUtils.generateAccessToken(user);
                customResponse.setSuccess(true);
                customResponse.setToken(token);
                customResponse.setMessage("Login Successful !");
                customResponse.setDetails(new ArrayList<>(List.of(user.get().getName(), user.get().getEmail())));
            } else {
                customResponse.setSuccess(false);
                customResponse.setMessage("Password is incorrect !");
                customResponse.setDetails(new ArrayList<>());
            }
        } else {
            customResponse.setSuccess(false);
            customResponse.setMessage("User not found. Check your email,");
            customResponse.setDetails(new ArrayList<>());
        }
        return ResponseEntity.status(customResponse.getSuccess() ? 200 : 403).body(customResponse);
    }
}
