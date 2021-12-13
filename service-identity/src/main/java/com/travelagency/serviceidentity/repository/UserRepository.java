package com.travelagency.serviceidentity.repository;

import com.travelagency.serviceidentity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
