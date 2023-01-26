package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.CreateUserRequestDto;
import ru.practicum.shareit.validationmarkers.OnCreate;
import ru.practicum.shareit.validationmarkers.OnUpdate;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final SmartValidator validator;
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequestDto requestDto, BindingResult errors) {
        validator.validate(requestDto, errors, OnCreate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return userClient.createUser(requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        return userClient.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id,
                                             @RequestBody CreateUserRequestDto userDto,
                                             BindingResult errors) {
        validator.validate(userDto, errors, OnUpdate.class);
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userClient.deleteUser(id);
    }


}
