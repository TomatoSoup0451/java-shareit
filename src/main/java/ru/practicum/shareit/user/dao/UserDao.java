package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User createUser(User user);

    User updateUser(long id, User user);

    List<User> getUsers();

    Optional<User> getUser(long id);

    void deleteUser(long id);
}
