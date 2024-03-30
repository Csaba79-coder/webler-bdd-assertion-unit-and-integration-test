package hu.webler.weblerbddassertionunitandintegrationtest.controller;

import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserLoginModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;
import hu.webler.weblerbddassertionunitandintegrationtest.service.UserService;
import hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserEntityToModel;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserRegistrationModel userRegistrationModel) {
        return ResponseEntity.status(201).body(userService.registerUser(userRegistrationModel));
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> loginUser(@RequestBody UserLoginModel userLoginModel) {
        return ResponseEntity.status(200).body(userService.loginUser(userLoginModel));
    }

    @GetMapping("/auth/users")
    public ResponseEntity<List<UserModel>> renderAllUsers() {
        return ResponseEntity.status(200).body(userService.findAllUsers());
    }

    @GetMapping("/auth/users/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(userService.findUserById(id));
    }

    @GetMapping("/auth/users/email/{email}")
    public ResponseEntity<UserModel> findUserByEmail(@PathVariable String email) {
        // TODO remove from here (follow other convention! and refactor in service usage after Mapper is done!)
        return ResponseEntity.status(200).body(mapUserEntityToModel(userService.findUserByEmail(email)));
    }
}
