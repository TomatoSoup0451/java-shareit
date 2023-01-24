package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validationmarkers.OnCreate;
import ru.practicum.shareit.validationmarkers.OnUpdate;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SmartValidator validator;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto, BindingResult errors) {
        validator.validate(userDto, errors, OnCreate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody UserDto userDto, BindingResult errors) {
        validator.validate(userDto, errors, OnUpdate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
