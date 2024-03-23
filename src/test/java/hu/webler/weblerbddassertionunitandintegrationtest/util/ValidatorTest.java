package hu.webler.weblerbddassertionunitandintegrationtest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

@DisplayName("Validator util - unit test")
class ValidatorTest {

    private final static String EMAIL_VALIDATOR_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

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
}