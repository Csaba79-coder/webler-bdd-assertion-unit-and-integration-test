package hu.webler.weblerbddassertionunitandintegrationtest.service;

import hu.webler.weblerbddassertionunitandintegrationtest.entity.base.Identifier;
import hu.webler.weblerbddassertionunitandintegrationtest.entity.user.User;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserLoginModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;
import hu.webler.weblerbddassertionunitandintegrationtest.persistence.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;

import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserRegModelToEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service test - unit test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Given valid user registration model when registering user then returns user model")
    public void givenValidUserRegistrationModel_whenRegisteringUser_thenReturnsUserModel() {
        // Given
        UserRegistrationModel userModel = new UserRegistrationModel();
        userModel.setEmail("test@example.com");
        userModel.setPassword("Almafa1234!");
        userModel.setRepeatedPassword("Almafa1234!");

        // Mock userRepository.save() to return a mock UserModel
        UserModel expectedModel = new UserModel();
        expectedModel.setId(UUID.randomUUID());
        expectedModel.setEmail("test@example.com");
        when(userRepository.save(any())).thenReturn(mapUserRegModelToEntity(userModel));

        // When
        UserModel registeredUserModel = userService.registerUser(userModel);

        // Then
        then(registeredUserModel.getEmail()).isEqualTo(userModel.getEmail());
        verify(userRepository, times(1)).save(any());
        then(expectedModel)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "createdBy", "updatedAt", "updatedBy")
                .isEqualTo(registeredUserModel);
    }

    @Test
    @DisplayName("Given null user model when try to register then throws illegal argument exception")
    public void givenNullUserModel_whenRegisterUser_thenThrowsIllegalArgumentException() {
        // Given
        UserRegistrationModel userModel = null;

        // When / Then
        assertThatThrownBy(() -> userService.registerUser(userModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("All fields must be provided");

        // Ensure userRepository.save() is not called
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given user model with empty email when try to register user then throws illegal argument exception")
    public void givenUserModelWithEmptyEmail_whenRegisterUser_thenThrowsIllegalArgumentException() {
        // Given
        UserRegistrationModel userModel = new UserRegistrationModel();
        userModel.setEmail("");
        userModel.setPassword("validPassword");
        userModel.setRepeatedPassword("validPassword");

        // When / Then
        assertThatThrownBy(() -> userService.registerUser(userModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("All fields must be provided");

        // Ensure userRepository.save() is not called
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given empty user list when get all users then returns empty list")
    public void givenEmptyUserList_whenGetAllUsers_thenReturnsEmptyList() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserModel> users = userService.findAllUsers();

        // Then
        then(users).isEmpty();
        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Given a non empty users' list when get all users then returns the list of user models")
    public void givenNonEmptyUserList_whenGetAllUsers_thenReturnsListOfUserModels() {
        // Given
        List<User> usersData = List.of(
                new User("user1", "password1"),
                new User("user2", "password2")
        );
        when(userRepository.findAll()).thenReturn(usersData);

        // When
        List<UserModel> users = userService.findAllUsers();

        // Then
        then(users).hasSize(2);
        assertThat(users).hasSize(2);
        assertThat(users)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "createdBy", "updatedAt", "updatedBy")
                .isEqualTo(usersData);
    }

    @Test
    @DisplayName("Given valid login user when login then returns user model")
    public void givenValidUserLoginModel_whenLoginUser_thenReturnsUserModel() {
        // Given
        UserLoginModel userLoginModel = new UserLoginModel("test@example.com", "password");
        User user = new User("test@example.com", "password");
        UserModel userModel = new UserModel(user.getId(), null, null, null, null, user.getEmail());

        when(userRepository.findUserByEmail(userLoginModel.getEmail())).thenReturn(Optional.of(user));

        // When
        UserModel result = userService.loginUser(userLoginModel);

        // Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "createdBy", "updatedAt", "updatedBy")
                .isEqualTo(userModel);
    }

    @Test
    @DisplayName("Given invalid login user when login then throws illegal argument exception")
    public void givenInvalidUserLoginModel_whenLoginUser_thenThrowsIllegalArgumentException() {
        // Given
        UserLoginModel userLoginModel = null;

        // When / Then
        assertThatThrownBy(() -> userService.loginUser(userLoginModel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email and password must be provided");
    }

    @Test
    @DisplayName("Given valid user id when find user by id then returns user model")
    public void givenValidUserId_whenFindUserById_thenReturnsUserModel() throws NoSuchFieldException, IllegalAccessException {
        // Given
        UUID testId = UUID.randomUUID();
        User user = new User();

        Field idField = Identifier.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, testId);
        user.setEmail("test@example.com");

        UserModel userModel = new UserModel(testId, null, null, null, null, "test@example.com");

        when(userRepository.findUserById(testId)).thenReturn(Optional.of(user));

        // When
        UserModel result = userService.findUserById(testId);

        // Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "createdBy", "updatedAt", "updatedBy")
                .isEqualTo(userModel);
    }

    @Test
    @DisplayName("Given invalid user id when findUserById then throws no such element exception")
    public void givenInvalidUserId_whenFindUserById_thenThrowsNoSuchElementException() {
        // Given
        UUID userId = UUID.randomUUID();

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.findUserById(userId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User with id " + userId + " is not found");
    }
    @Test
    @DisplayName("Given valid email when findUserByEmail then returns user")
    public void givenValidEmail_whenFindUserByEmail_thenReturnsUser() {
        // Given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("Almafa1234?")
                .build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        // When
        User result = userService.findUserByEmail(email);

        // Then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Given invalid email when findUserByEmail then throws no such element exception")
    public void givenInvalidEmail_whenFindUserByEmail_thenThrowsNoSuchElementException() {
        // Given
        String email = "nonexistent@example.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.findUserByEmail(email))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User with email " + email + " is not found");
    }
}