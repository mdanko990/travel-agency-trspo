package com.travelagency.serviceidentity.service.impl;


import com.travelagency.serviceidentity.model.User;
import com.travelagency.serviceidentity.model.enums.UserType;
import com.travelagency.serviceidentity.repository.UserRepository;
import com.travelagency.serviceidentity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) throws IllegalArgumentException {
        final Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            throw new IllegalArgumentException("Invalid user ID");
    }

    @Override
    public long createUser(String firstname, String lastname, String email,String password, Integer age, UserType type) {
        final User user = new User(firstname, lastname, email, password, age, type);
        final User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public void updateUser(Long id, String firstname, String lastname, String email,String password, Integer age, UserType type)
            throws IllegalArgumentException {
        final Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("Invalid user ID");

        final User user = optionalUser.get();
        if (firstname != null && !firstname.isBlank()) user.setFirstname(firstname);
        if (lastname != null && !lastname.isBlank()) user.setLastname(lastname);
        if (password != null && !password.isBlank()) user.setPassword(password);
        if (age != null) user.setAge(age);
        if (type != null && type != UserType.NULL) user.setType(type);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
