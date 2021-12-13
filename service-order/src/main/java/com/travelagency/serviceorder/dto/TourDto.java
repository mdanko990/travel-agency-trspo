package com.travelagency.serviceorder.dto;

public record TourDto (String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId){
}
