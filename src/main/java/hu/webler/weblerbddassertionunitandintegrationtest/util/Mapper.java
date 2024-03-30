package hu.webler.weblerbddassertionunitandintegrationtest.util;

import hu.webler.weblerbddassertionunitandintegrationtest.entity.user.User;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserModel;
import hu.webler.weblerbddassertionunitandintegrationtest.model.user.UserRegistrationModel;

public class Mapper {

    public static User mapUserRegModelToEntity(UserRegistrationModel model) {
        return User
                .builder()
                .email(model.getEmail())
                .password(model.getPassword())
                .build();
    }

    public static UserModel mapUserEntityToModel(User entity) {
        return UserModel
                .builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .email(entity.getEmail())
                .build();
    }

    private Mapper() {

    }
}
