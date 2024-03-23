package hu.webler.weblerbddassertionunitandintegrationtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private UUID id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private String email;
}
