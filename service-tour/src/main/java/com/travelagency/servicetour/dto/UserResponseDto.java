package com.travelagency.servicetour.dto;

import com.travelagency.servicetour.enums.UserType;

public record UserResponseDto(String firstname, String lastname, String email, String password, Integer age, UserType userType) {

}
