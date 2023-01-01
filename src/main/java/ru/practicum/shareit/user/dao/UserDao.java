package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);

    User updateUser(Long id, User user);

    List<User> getUsers();

    User getUser(long id);

    User deleteUser(long id);
}
