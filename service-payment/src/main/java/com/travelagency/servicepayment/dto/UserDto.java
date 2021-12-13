package com.travelagency.servicepayment.dto;

import com.travelagency.servicepayment.enums.UserType;

public record UserDto(String name, String password, String passportId, UserType userType) {
}

