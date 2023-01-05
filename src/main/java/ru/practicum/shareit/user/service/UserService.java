package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUser(long id);

    User createUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}
