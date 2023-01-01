package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User createUser(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("User with id = " + user.getId() + " added");
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new IdNotFoundException("User with id = " + user.getId() + " not found");
        } else if (users.values().stream()
                .filter(i -> i.getEmail().equals(user.getEmail()))
                .anyMatch(i -> i.getId() != id)) {
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists");
        }
        User result = users.get(id);
        if (user.getName() != null && !user.getName().isEmpty()) {
            result.setName(user.getName());
        }
        if (user.getEmail() != null) {
            result.setEmail(user.getEmail());
        }
        users.put(id, result);
        log.info("User with id = " + user.getId() + " updated");
        return result;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new IdNotFoundException("User with id = " + id + " not found");
        }
        return users.get(id);
    }

    @Override
    public User deleteUser(long id) {
        if (!users.containsKey(id)) {
            throw new IdNotFoundException("User with id = " + id + " not found");
        }
        User result = users.get(id);
        users.remove(id);
        return result;
    }
}
