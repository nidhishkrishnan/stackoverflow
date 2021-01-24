package com.employee.mapper;

import com.employee.domain.UserDTO;
import com.employee.model.User;

public class UserMapper {
    public static UserDTO mapToUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
