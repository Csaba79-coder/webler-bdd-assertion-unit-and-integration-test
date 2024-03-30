package hu.webler.weblerbddassertionunitandintegrationtest.model.user;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    private UUID id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private String email;
}
