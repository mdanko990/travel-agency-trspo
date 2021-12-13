package com.travelagency.servicetour.controller;

import com.travelagency.servicetour.dto.TourDto;
import com.travelagency.servicetour.dto.TourFullInfoDto;
import com.travelagency.servicetour.model.Tour;
import com.travelagency.servicetour.service.impl.TourServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/tour")
@RestController
public class TourController {
    private final TourServiceImpl tourService;

    @GetMapping
    public ResponseEntity<List<Tour>> getAll() {
        final List<Tour> tours = tourService.getAll();
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getById(@PathVariable long id) {
        try {
            Tour tour = tourService.getTourById(id);
            return ResponseEntity.ok(tour);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> change(@RequestBody TourDto tourDto) {
        final String name = tourDto.name();
        final String startDate = tourDto.startDate();
        final String lastDate = tourDto.lastDate();
        final String route = tourDto.route();
        final Integer cost = tourDto.cost();
        final Integer numberOfSeats = tourDto.numberOfSeats();
        final Long guiderId = tourDto.guiderId();

        try {
            final long tourId = tourService.createTour(name, startDate, lastDate, route,
                    cost, numberOfSeats, guiderId);
            final String tourUri = "/tour/" + tourId;

            return ResponseEntity.created(URI.create(tourUri)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> change(@PathVariable long id,
                                       @RequestBody TourDto tourDto) {
        final long guiderId;
        if (tourDto.guiderId() == null) {
            guiderId = -1;
        }
        else {
            guiderId = tourDto.guiderId();
        }

        final String name = tourDto.name();
        final String startDate = tourDto.startDate();
        final String lastDate = tourDto.lastDate();
        final String route = tourDto.route();
        final Integer cost = tourDto.cost();
        final Integer numberOfSeats = tourDto.numberOfSeats();

        try {
            tourService.updateTour(id, name, startDate, lastDate, route,
                    cost, numberOfSeats, guiderId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<TourFullInfoDto>> getAllInfo() {
        final List<TourFullInfoDto> tourFullInfoDtos = tourService.getAllFullInfo();
        return ResponseEntity.ok(tourFullInfoDtos);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<TourFullInfoDto> getInfoById(@PathVariable long id) {
        try {
            TourFullInfoDto tourFullInfoDto = tourService.getFullInfoById(id);
            return ResponseEntity.ok(tourFullInfoDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
