package hu.webler.weblerbddassertionunitandintegrationtest.service;

import hu.webler.weblerbddassertionunitandintegrationtest.entity.user.User;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserLoginModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;
import hu.webler.weblerbddassertionunitandintegrationtest.persistence.UserRepository;
import hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserEntityToModel;
import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserRegModelToEntity;
import static hu.webler.weblerbddassertionunitandintegrationtest.util.Validator.patternMatches;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final static String EMAIL_VALIDATOR_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final static String PASSWORD_VALIDATOR_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.?])(?=\\S+$).{8,20}$";

    private final UserRepository userRepository;

    public UserModel registerUser(UserRegistrationModel userModel) throws IllegalArgumentException {
        // Check if any field is null or blank
        // TODO for existing user! check if user exists!
        if (userModel == null || userModel.getEmail().isEmpty() || userModel.getPassword().isEmpty() || userModel.getRepeatedPassword().isEmpty()
                || userModel.getEmail().isBlank() || userModel.getPassword().isBlank() || userModel.getRepeatedPassword().isBlank()) {
            String message = "All fields must be provided";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        // Check if password and repeatedPassword match
        if (!userModel.getPassword().equals(userModel.getRepeatedPassword())) {
            String message = "Passwords do not match";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        // Validate email pattern
        if (!patternMatches(userModel.getEmail(), EMAIL_VALIDATOR_PATTERN)) {
            String message = "Invalid email address format";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        // Validate password pattern
        if (!patternMatches(userModel.getPassword(), PASSWORD_VALIDATOR_PATTERN)) {
            String message = "Invalid password format";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        return mapUserEntityToModel(userRepository.save(mapUserRegModelToEntity(userModel)));
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(Mapper::mapUserEntityToModel)
                .collect(Collectors.toList());
    }

    public UserModel loginUser(UserLoginModel userLoginModel) {
        // Validate login credentials
        if (userLoginModel == null || isEmpty(userLoginModel.getEmail())
                || isEmpty(userLoginModel.getPassword())) {
            String message = "Email and password must be provided";
            log.info(message);
            throw new IllegalArgumentException(message);
        }
        return ResponseEntity.status(200).body(mapUserEntityToModel(findUserByEmail(userLoginModel.getEmail()))).getBody();
    }

    public UserModel findUserById(UUID id) {
        return Mapper.mapUserEntityToModel(userRepository.findUserById(id)
                .orElseThrow(() -> {
                    String message = String.format("User with id %s is not found", id);
                    log.info(message);
                    return new NoSuchElementException(message);
                }));
    }

    // TODO make UserMode here, and map back to User !!!
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    String message = String.format("User with email %s is not found", email);
                    log.info(message);
                    return new NoSuchElementException(message);
                });
    }
}
