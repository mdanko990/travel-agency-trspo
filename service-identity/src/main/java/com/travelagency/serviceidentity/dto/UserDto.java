package com.travelagency.serviceidentity.dto;

import com.travelagency.serviceidentity.model.enums.UserType;

public record UserDto(String firstname, String lastname, String email, String password, Integer age, int userType) {
}
