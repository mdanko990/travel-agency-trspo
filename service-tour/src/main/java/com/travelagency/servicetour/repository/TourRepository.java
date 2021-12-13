package com.travelagency.servicetour.repository;

import com.travelagency.servicetour.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
