package com.travelagency.servicetour.dto;

public record TourFullInfoDto(String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, String guiderName) {
}
