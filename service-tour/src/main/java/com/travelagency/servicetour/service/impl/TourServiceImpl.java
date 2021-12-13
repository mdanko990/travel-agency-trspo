package com.travelagency.servicetour.service.impl;

import com.travelagency.servicetour.dto.TourFullInfoDto;
import com.travelagency.servicetour.dto.UserResponseDto;
import com.travelagency.servicetour.enums.UserType;
import com.travelagency.servicetour.model.Tour;
import com.travelagency.servicetour.repository.TourRepository;
import com.travelagency.servicetour.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements TourService {
    private final String userUrlAdress = "http://localhost:8080/user";

    private final TourRepository tourRepository;

    @Override
    public List<Tour> getAll() {
        return tourRepository.findAll();
    }

    @Override
    public Tour getTourById(long id) throws IllegalArgumentException {
        final Optional<Tour> optionalFeedback = tourRepository.findById(id);

        if (optionalFeedback.isPresent()) {
            return optionalFeedback.get();
        }
        else {
            throw new IllegalArgumentException("Invalid tour id");
        }
    }

    private boolean checkForCorrectness(Long guiderId, UserType userType) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(guiderId);

        final ResponseEntity<UserResponseDto> userResponse = restTemplate
                .exchange(userUrlAdress + "/dto/" + guiderId,
                        HttpMethod.GET, userRequest, UserResponseDto.class);

        return userResponse.getStatusCode() != HttpStatus.NOT_FOUND
                && userResponse.getBody().userType() == userType;
    }

    @Override
    public long createTour(String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId) throws IllegalArgumentException {
        final Tour tour = new Tour(name, startDate, lastDate, route, cost, numberOfSeats, guiderId);

        final UserResponseDto userResponseDto = getUserResponseById(guiderId);

        if (!checkForCorrectness(guiderId, userResponseDto.userType())) {
            throw new IllegalArgumentException("Bad request");
        }
        else {
            final Tour savedFeedback = tourRepository.save(tour);
            return savedFeedback.getId();
        }
    }

    @Override
    public void updateTour(Long id, String name, String startDate, String lastDate, String route, Integer cost, Integer numberOfSeats, Long guiderId) throws IllegalArgumentException {
        final Optional<Tour> optionalTour = tourRepository.findById(id);

        if (optionalTour.isEmpty()) {
            throw new IllegalArgumentException("Invalid tour id");
        }

        final Tour tour = optionalTour.get();

        final UserResponseDto userResponseDto = getUserResponseById(guiderId);

        if (guiderId != null && guiderId != -1) {
            if(!checkForCorrectness(guiderId, userResponseDto.userType())) {
                throw new IllegalArgumentException("Invalid user id or type");
            }

            tour.setGuiderId(guiderId);
        }
        if (name != null && !name.isBlank()) tour.setName(name);
        if (startDate != null && !startDate.isBlank()) tour.setName(startDate);
        if (lastDate != null && !lastDate.isBlank()) tour.setName(lastDate);
        if (route != null && !route.isBlank()) tour.setName(route);

        if (cost != 0) {
            tour.setCost(cost);
        }
        tourRepository.save(tour);
    }

    @Override
    public void deleteTour(Long id) {tourRepository.deleteById(id);}

    private UserResponseDto getUserResponseById(Long id) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> userRequest = new HttpEntity<>(id);

        final ResponseEntity<UserResponseDto> userResponseDtoEntity = restTemplate
                .exchange(userUrlAdress + "/" + id,
                        HttpMethod.GET, userRequest, UserResponseDto.class);

        return userResponseDtoEntity.getBody();
    }

    private TourFullInfoDto tourToFullInfo(Tour tour) {
        final UserResponseDto userResponseDto
                = getUserResponseById(tour.getGuiderId());
        final String name = tour.getName();
        final String startDate = tour.getStartDate();
        final String lastDate = tour.getLastDate();
        final String route = tour.getRoute();
        final Integer cost = tour.getCost();
        final Integer numberOfSeats = tour.getNumberOfSeats();
        final String guiderName = userResponseDto.firstname() + ' ' + userResponseDto.lastname();

        return new TourFullInfoDto(name, startDate, lastDate, route, cost, numberOfSeats, guiderName);
    }

    @Override
    public List<TourFullInfoDto> getAllFullInfo() {
        List<Tour> tours = getAll();
        List<TourFullInfoDto> tourFullInfoDtos = new ArrayList<>();

        for (Tour tour : tours) {
            tourFullInfoDtos.add(tourToFullInfo(tour));
        }

        return tourFullInfoDtos;
    }

    @Override
    public TourFullInfoDto getFullInfoById(Long id) throws IllegalArgumentException {
        try {
            Tour tour = getTourById(id);
            return tourToFullInfo(tour);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
