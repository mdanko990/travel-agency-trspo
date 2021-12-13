package com.travelagency.serviceorder.repository;

import com.travelagency.serviceorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
