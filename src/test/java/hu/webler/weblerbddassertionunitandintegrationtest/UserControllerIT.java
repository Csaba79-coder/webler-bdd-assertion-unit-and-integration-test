package hu.webler.weblerbddassertionunitandintegrationtest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.webler.weblerbddassertionunitandintegrationtest.controller.UserController;
import hu.webler.weblerbddassertionunitandintegrationtest.entity.user.User;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserLoginModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;
import hu.webler.weblerbddassertionunitandintegrationtest.service.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
@ExtendWith(MockitoExtension.class)
@DisplayName("User controller - Integration test")
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Given valid user registration details when register user then status created")
    public void givenValidUserRegistrationDetails_whenRegisterUser_thenStatusCreated() throws Exception {
        // Given
        UserRegistrationModel userRegistrationModel
                = new UserRegistrationModel("test@example.com", "Almafa1234?", "Almafa1234?");

        // Mock the userService to return a registered user model
        UserModel registeredUser = new UserModel(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(),
                UUID.randomUUID(), UUID.randomUUID(), userRegistrationModel.getEmail());
        when(userService.registerUser(any(UserRegistrationModel.class))).thenReturn(registeredUser);

        // When
        MvcResult result = mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationModel)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isNotEmpty();
        assertThat(responseContent).isNotBlank();
        assertThat(responseContent).isEqualTo(objectMapper.writeValueAsString(registeredUser));
        assertThat(responseContent).contains(userRegistrationModel.getEmail());
    }

    @Test
    @DisplayName("Given valid user login details when login user then status OK")
    public void givenValidUserLoginDetails_whenLoginUser_thenStatusOK() throws Exception {
        // Given
        UserLoginModel userLoginModel = new UserLoginModel("test@test", "correctPassword");

        // Mock the userService to return a logged-in user model
        UserModel loggedInUser = new UserModel(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(),
                UUID.randomUUID(), UUID.randomUUID(), userLoginModel.getEmail());
        when(userService.loginUser(any(UserLoginModel.class))).thenReturn(loggedInUser);

        // When
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginModel)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).isNotEmpty();
        assertThat(responseContent).isNotBlank();
        assertThat(responseContent).isEqualTo(objectMapper.writeValueAsString(loggedInUser));
        assertThat(responseContent).contains(userLoginModel.getEmail());
    }

    @Test
    @DisplayName("Given invalid user registration details when register user then status bad request")
    public void givenInvalidUserRegistrationDetails_whenRegisterUser_thenStatusBadRequest() throws Exception {
        // Given
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("test@test", "Almafa1234?",
                "Almafa1234!");

        // When passwords do not match, throw an IllegalArgumentException with the message "Passwords do not match"
        when(userService.registerUser(any(UserRegistrationModel.class)))
                .thenAnswer(invocation -> {
                    UserRegistrationModel arg = invocation.getArgument(0);
                    if (!arg.getPassword().equals(arg.getRepeatedPassword())) {
                        throw new IllegalArgumentException("Passwords do not match");
                    }
                    return null;
                });

        // When/Then
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRegistrationModel)));
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Passwords do not match", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Given invalid user login details when login user then status bad request")
    public void givenInvalidUserLoginDetails_whenLoginUser_thenStatusBadRequest() throws Exception {
        // Given
        UserLoginModel userLoginModel = new UserLoginModel("test@test", "wrongPassword");

        // When email or password is incorrect, throw an IllegalArgumentException with the message "Invalid login details"
        when(userService.loginUser(any(UserLoginModel.class)))
                .thenAnswer(invocation -> {
                    UserLoginModel arg = invocation.getArgument(0);
                    if (!arg.getPassword().equals("correctPassword")) {
                        throw new IllegalArgumentException("Invalid login details");
                    }
                    return null;
                });

        // When/Then
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userLoginModel)));
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Invalid login details", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Given empty list when findAllUsers then return empty list")
    public void givenEmptyList_whenFetchingAllUsers_thenReturnEmptyList() throws Exception {
        // Given
        given(userService.findAllUsers()).willReturn(Collections.emptyList());
        List<UserModel> expectedModels = new ArrayList<>();

        // When
        MvcResult result = mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<UserModel> actualModels = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(actualModels)
                .usingRecursiveComparison()
                .asList()
                .isEmpty();
        assertThat(actualModels).isEqualTo(expectedModels);
        assertThat(actualModels)
                .usingRecursiveComparison()
                .asList()
                .hasSize(0);
    }

    @Test
    @DisplayName("Given non-empty list when findAllUsers then return the list")
    public void givenNonEmptyList_whenFetchingAllUsers_thenReturnNonEmptyList() throws Exception {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        List<UserModel> expectedModels = Arrays.asList(
                new UserModel(userId1, now, now, UUID.randomUUID(), UUID.randomUUID(), "john@example.com"),
                new UserModel(userId2, now, now, UUID.randomUUID(), UUID.randomUUID(), "jane@example.com")
        );

        given(userService.findAllUsers()).willReturn(expectedModels);

        // When
        MvcResult result = mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<UserModel> actualModels = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(actualModels)
                .usingRecursiveComparison()
                .asList()
                .isNotEmpty(); // Assert that the list is not empty
        assertThat(actualModels)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt") // Ignore these fields
                .isEqualTo(expectedModels); // Assert that the lists are equal
        assertThat(actualModels)
                .usingRecursiveComparison()
                .asList()
                .hasSize(expectedModels.size()); // Assert that the size of actual list matches the size of expected list
    }

    @Test
    @DisplayName("Given valid email when findUserByEmail then return user")
    public void givenValidEmail_whenFindUserByEmail_thenReturnUser() throws Exception {
        // Given
        String email = "testelek@gmail.com"; // Updated valid email

        // Mock the userService to return a user model
        User user = new User(email, "password");
        when(userService.findUserByEmail(any(String.class))).thenReturn(user); // Assuming it's findUserByEmail instead of findUserByIEmail

        // When
        MvcResult result = mockMvc.perform(get("/api/auth/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertThat(email).isNotNull();
        assertThat(email).isNotBlank();
        assertThat(email).isEqualTo(user.getEmail());

        String responseContent = result.getResponse().getContentAsString();
        UserModel actualUser = objectMapper.readValue(responseContent, UserModel.class);

        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "createdBy", "updatedBy")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Given invalid email when findUserByEmail then throw IllegalArgumentException")
    public void givenInvalidEmail_whenFindUserByEmail_thenThrowIllegalArgumentException() throws Exception {
        // Given
        String invalidEmail = "invalidEmail";

        // When the email is invalid, throw an IllegalArgumentException with the message "Invalid email"
        when(userService.findUserByEmail(any(String.class)))
                .thenAnswer(invocation -> {
                    String arg = invocation.getArgument(0);
                    if (!arg.contains("@")) {
                        throw new IllegalArgumentException("Invalid email");
                    }
                    return null;
                });

        // When/Then
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/auth/users/email/{email}", invalidEmail)
                    .contentType(MediaType.APPLICATION_JSON));
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Invalid email", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Given non-existing email when findUserByEmail then throw NoSuchElementException")
    public void givenNonExistingEmail_whenFindUserByEmail_thenThrowNoSuchElementException() throws Exception {
        // Given
        String nonExistingEmail = "nonExisting@test.com";
        String errorMessage = String.format("User with email %s is not found", nonExistingEmail);

        // When the email does not exist, throw a NoSuchElementException with the message "User with email {email} is not found"
        when(userService.findUserByEmail(any(String.class)))
                .thenAnswer(invocation -> {
                    String arg = invocation.getArgument(0);
                    if (arg.equals(nonExistingEmail)) {
                        throw new NoSuchElementException(errorMessage);
                    }
                    return null;
                });

        // When/Then
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/auth/users/email/{email}", nonExistingEmail)
                    .contentType(MediaType.APPLICATION_JSON));
        });

        assertInstanceOf(NoSuchElementException.class, exception.getCause());
        assertEquals(errorMessage, exception.getCause().getMessage());
    }
}
