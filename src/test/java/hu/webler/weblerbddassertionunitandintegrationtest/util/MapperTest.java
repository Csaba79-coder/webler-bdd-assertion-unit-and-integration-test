package hu.webler.weblerbddassertionunitandintegrationtest.util;

import hu.webler.weblerbddassertionunitandintegrationtest.entity.base.Auditable;
import hu.webler.weblerbddassertionunitandintegrationtest.entity.base.Identifier;
import hu.webler.weblerbddassertionunitandintegrationtest.entity.user.User;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserEntityToModel;
import static hu.webler.weblerbddassertionunitandintegrationtest.util.Mapper.mapUserRegModelToEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

@DisplayName("Mapper uti - unit test")
class MapperTest {

    @Test
    @DisplayName("Given user registration model when mapping to entity then returns user entity with encrypted password")
    public void givenUserRegistrationModel_whenMappingToEntity_thenReturnsUserEntityWithEncryptedPassword() {
        // Given
        UserRegistrationModel model = new UserRegistrationModel();
        model.setEmail("test@example.com");
        model.setPassword("Password123!");
        model.setRepeatedPassword("Password123!");

        // When
        User entity = mapUserRegModelToEntity(model);

        // Then
        then(model.getEmail())
                .isEqualTo(entity.getEmail());
        /*BDDMockito
                .then(model.getPassword())
                        .equals(entity.getPassword());*/

        // This is useful, if it has more thane one field, and not only checking the email match!!!
        then(model)
                .usingRecursiveComparison()
                        .ignoringFields("password", "repeatedPassword")
                                .isEqualTo(entity);

        // This is not BDD assertion!!! -> check imports ... (import static org.assertj.core.api.Assertions.assertThat;)
        assertThat(entity)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "createdBy",
                        "updatedBy", "password", "repeatedPassword")
                .isEqualTo(model);
    }

    @Test
    @DisplayName("Given user entity when mapping to model then returns user model with same attributes")
    public void givenUserEntity_whenMappingToModel_thenReturnsUserModelWithSameAttributes() throws NoSuchFieldException, IllegalAccessException {
        // Given
        User user = new User();
        LocalDateTime now = LocalDateTime.parse("2024-03-22T13:40:41.749859");
        // Generate a UUID for testing
        UUID testId = UUID.fromString("d1b4dd3e-d808-4c1a-aced-f4943cc0226c");

        // Use reflection to set the ID value
        Field idField = Identifier.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, testId);

        Field createdTimeStamp = Auditable.class.getDeclaredField("createdAt");
        createdTimeStamp.setAccessible(true);
        createdTimeStamp.set(user, now);

        Field updatedTimeStamp = Auditable.class.getDeclaredField("updatedAt");
        updatedTimeStamp.setAccessible(true);
        updatedTimeStamp.set(user, now);

        Field createdBy = Auditable.class.getDeclaredField("createdBy");
        createdBy.setAccessible(true);
        createdBy.set(user, testId);

        Field updatedBy = Auditable.class.getDeclaredField("updatedBy");
        updatedBy.setAccessible(true);
        updatedBy.set(user, testId);

        user.setEmail("test@example.com");
        user.setPassword("Almafa1234?");

        // When
        UserModel model = mapUserEntityToModel(user);

        // Then
        UserModel expectedModel = UserModel.builder()
                .id(testId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy(testId)
                .updatedBy(testId)
                .email("test@example.com")
                .build();

        // expected then result
        then(expectedModel)
                .usingRecursiveComparison()
                .isEqualTo(model);

        // result and then expected
        assertThat(model)
                .usingRecursiveComparison()
                .isEqualTo(expectedModel);
    }
}
