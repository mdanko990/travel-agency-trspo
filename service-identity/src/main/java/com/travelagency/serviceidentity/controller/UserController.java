package com.travelagency.serviceidentity.controller;

import com.travelagency.serviceidentity.dto.UserDto;
import com.travelagency.serviceidentity.model.enums.UserType;
import com.travelagency.serviceidentity.model.User;
import com.travelagency.serviceidentity.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        final List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {
        try {
            User user = userService.getUserById(id);

            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<UserDto> getDtoById(@PathVariable long id) {
        try {
            User user = userService.getUserById(id);
            UserDto userDto = new UserDto(user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(),
                    user.getAge(), user.getType().ordinal());

            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
        final String firstname = userDto.firstname();
        final String lastname = userDto.lastname();
        final String email = userDto.email();
        final String password = userDto.password();
        final Integer age = userDto.age();
        final int userType = userDto.userType();

        final long userId = userService.createUser(firstname, lastname, email, password, age,
                UserType.values()[userType]);
        final String userUri = "/user" + userId;

        return ResponseEntity.created(URI.create(userUri)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id, @RequestBody UserDto userDto) {
        final String firstname = userDto.firstname();
        final String lastname = userDto.lastname();
        final String email = userDto.email();
        final String password = userDto.password();
        final Integer age = userDto.age();
        final UserType type = UserType.values()[userDto.userType()];

        try {
            userService.updateUser(id, firstname, lastname, email, password, age, type);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

}
