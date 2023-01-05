package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<UserDto> getUsers() {
        return userDao.getUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userDao.getUser(id)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + id + " not found")));
    }

    @Override
    public User createUser(UserDto userDto) {
        return userDao.createUser(UserMapper.toUser(userDto));
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        return userDao.updateUser(id, UserMapper.toUser(userDto));
    }

    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }
}
