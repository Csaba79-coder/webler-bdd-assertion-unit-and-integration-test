package hu.webler.weblerbddassertionunitandintegrationtest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hu.webler.weblerbddassertionunitandintegrationtest.util.Encryptor.encryptPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test encryptor util - unit test")
class EncryptorTest {

    @Test
    @DisplayName("Given password when encrypt password then returns encrypted password")
    public void givenPassword_whenEncryptPassword_thenReturnEncryptedPassword() {
        // Given
        String password = "password1234";
        String password2 = "password123";

        // When
        String encryptedPassword = encryptPassword(password);
        String encryptedPassword2 = encryptPassword(password2);
        String encryptedPasswordAgain = encryptPassword(password);

        // Then
        then(encryptedPassword)
                .isNotNull()
                .isNotEmpty();

        assertThat(encryptedPassword)
                .isNotNull();
        assertThat(encryptedPassword)
                .isNotEmpty();

        then(encryptedPassword)
                .hasSize(32);

        then(encryptedPassword2)
                .hasSize(32);
        assertThat(encryptedPassword2)
                .hasSize(32);
        assertEquals(encryptedPassword2.length(), 32);

        then(encryptedPasswordAgain)
                .isEqualTo(encryptedPassword);
    }

    @Test
    @DisplayName("Given password is null or empty string or blank when encrypt password then returns illegal argument exception")
    public void givenPasswordIsNullOrEmptyStringOrBlank_whenEncryptPassword_thenReturnsIllegalArgumentException() {
        // Given
        String nullPassword = null;
        String emptyStringPassword = "";
        String blankPassword = " ";

        // When / Then
        thenThrownBy(() ->
                encryptPassword(nullPassword))
                .isInstanceOf(IllegalArgumentException.class);
        thenThrownBy(() ->
                encryptPassword(emptyStringPassword))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                encryptPassword(nullPassword))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                encryptPassword(emptyStringPassword))
                .isInstanceOf(IllegalArgumentException.class);

        thenThrownBy(() ->
                encryptPassword(blankPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }
}