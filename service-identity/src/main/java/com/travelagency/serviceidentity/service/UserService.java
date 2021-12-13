package com.travelagency.serviceidentity.service;

import com.travelagency.serviceidentity.model.enums.UserType;
import com.travelagency.serviceidentity.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(long id) throws IllegalArgumentException;
    long createUser(String firstname, String lastname, String email,String password, Integer age, UserType type);
    void updateUser(Long id, String firstname, String lastname, String email,String password, Integer age, UserType type)
            throws IllegalArgumentException;
    void deleteUser(long id);
}
