package com.travelagency.servicetour.service;

import com.travelagency.servicetour.dto.TourFullInfoDto;
import com.travelagency.servicetour.model.Tour;

import java.util.List;

public interface TourService {
    List<Tour> getAll();
    Tour getTourById(long id) throws IllegalArgumentException;
    long createTour(String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId)
            throws IllegalArgumentException;
    void updateTour(Long id, String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId)
            throws IllegalArgumentException;
    void deleteTour(Long id);
    List<TourFullInfoDto> getAllFullInfo();
    TourFullInfoDto getFullInfoById(Long id) throws IllegalArgumentException;
}
