package hu.webler.weblerbddassertionunitandintegrationtest.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationModel {

    private String email;
    private String password;
    private String repeatedPassword;
}
