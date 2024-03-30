package hu.webler.weblerbddassertionunitandintegrationtest.controller.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.assertj.core.api.BDDAssertions.then;

@DisplayName("Controller Exception Handling test")
class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();

    @Test
    @DisplayName("Given no such element exception and when handling it then returns not found response")
    public void givenNoSuchElementException_whenHandling_thenReturnsNotFoundResponse() {
        // Given
        NoSuchElementException exception = new NoSuchElementException("Element not found");

        // When
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.handleNoSuchElementException(exception);

        // Then
        then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(responseEntity.getBody()).isEqualTo("{ERROR_CODE_001=Element not found}");
    }

    @Test
    @DisplayName("Given illegal argument exception and when handling it then returns bad request response")
    public void givenIllegalArgumentException_whenHandling_thenReturnsBadRequestResponse() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<Object> responseEntity = controllerExceptionHandler.handleIllegalArgumentException(exception);

        // Then
        then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(responseEntity.getBody()).isEqualTo("{ERROR_CODE_002=Invalid argument}");
    }
}