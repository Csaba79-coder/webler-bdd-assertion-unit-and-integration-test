package hu.webler.weblerbddassertionunitandintegrationtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// TODO not used in project implement later!
// in this scenario the token and expiry date must be saved into database to the user itself!
public class UserForgottenPassword {

    private String token;
    private Date expiryDate;
    private String password;
    private String repeatedPassword;
}
