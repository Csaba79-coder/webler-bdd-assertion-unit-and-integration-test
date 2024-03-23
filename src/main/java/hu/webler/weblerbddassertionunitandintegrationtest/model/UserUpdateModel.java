package hu.webler.weblerbddassertionunitandintegrationtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// TODO not used in project implement later!
// incoming model!
public class UserUpdateModel {

    private String password;
    private String updatedPassword;
    private String updatedRepeatedPassword;
}
