package com.travelagency.serviceorder.dto;

import com.travelagency.serviceorder.enums.UserType;

public record UserDto(String firstname, String lastname, String email, String password, Integer age, UserType userType) {
}

