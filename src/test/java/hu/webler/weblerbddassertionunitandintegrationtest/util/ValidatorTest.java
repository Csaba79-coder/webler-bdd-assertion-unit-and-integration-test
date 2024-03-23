package hu.webler.weblerbddassertionunitandintegrationtest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Validator util - unit test")
class ValidatorTest {

    private final static String EMAIL_VALIDATOR_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final static String PASSWORD_VALIDATOR_PATTERN
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.?])(?=\\S+$).{8,20}$";

    @Test
    @DisplayName("Given valid email pattern and regex pattern when matching pattern returns true")
    public void givenValidEmailPattern_whenMatchingPattern_thenReturnsTrue() {
        // Given
        String validEmail = "test@exmaple.com";
        String validUkEmail = "test@something.co.uk";
        String validHuEmail = "test@gmail.hu";

        // When
        boolean isMatching = Validator.patternMatches(validEmail, EMAIL_VALIDATOR_PATTERN);
        boolean isValidUkEmail = Validator.patternMatches(validUkEmail, EMAIL_VALIDATOR_PATTERN);
        boolean isValidHuEmail = Validator.patternMatches(validHuEmail, EMAIL_VALIDATOR_PATTERN);

        // Then
        then(isMatching).isTrue();
        then(isValidUkEmail).isTrue();
        then(isValidHuEmail).isTrue();
        assertThat(isMatching).isTrue();
        assertTrue(isMatching);
    }

    @Test
    @DisplayName("Given invalid meail pattern and regex pattern when matching pattern then returns false")
    public void givenInvalidEmailPattern_whenMatchingPattern_thenReturnsFalse() {
        // Given
        String emptyEmail = "";

        // When
        boolean isValidEmptyEmail = Validator.patternMatches(emptyEmail, EMAIL_VALIDATOR_PATTERN);

        // Then
        then(isValidEmptyEmail).isFalse();

        // Given
        String[] invalidEmails = {
                null,
                "",
                "invalid.email.com",
                "example.com",
                "test user@example.com",
                "test!user@example.com",
                "test@exmaple",
                "test@sub..example.com",
                "test@@gmail.com",
                "@gmail.com",
                "?@gmail.com"
        };

        // When / Then
        for (String email : invalidEmails) {
            boolean isMatching = Validator.patternMatches(email, EMAIL_VALIDATOR_PATTERN);
            then(isMatching).isFalse();
        }
    }

    @Test
    @DisplayName("Given valid password pattern and regex pattern when matching pattern then returns true")
    public void givenValidPasswordPattern_whenMatchingPattern_thenReturnTrue() {
        // Given
        String[] validPasswords = {
                "Almafa12134?",
                "?Almafa1234",
                "almaFa12@34",
                "1234Almafa!!"
        };

        // When / Then
        for (String password : validPasswords) {
            boolean isMatching = Validator.patternMatches(password, PASSWORD_VALIDATOR_PATTERN);
            then(isMatching).isTrue();
            assertTrue(isMatching);
        }
    }

    @Test
    @DisplayName("Given invalid password pattern and regex pattern when matching pattern then returns false")
    public void givenInvalidPasswordPattern_whenMatchingRegexPattern_thenReturnFalse() {
        // Given
        String[] invalidPasswords = {
                null,
                "",
                "password",
                "pa12@H",
                "Almafa1234",
                "Abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+Abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+",
                "abcdefg1!",
                "ABCDEFG1!",
                "Abcdefg!",
                "Abcdefg1"
        };

        // When / Then
        for (String password : invalidPasswords) {
            boolean isValid = Validator.patternMatches(password, PASSWORD_VALIDATOR_PATTERN);
            then(isValid).isFalse();
        }
    }
}