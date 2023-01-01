package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<UserDto> getUsers() {
        return userDao.getUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userDao.getUser(id));
    }

    public User createUser(UserDto userDto) {
        return userDao.createUser(UserMapper.toUser(userDto));
    }

    public User updateUser(Long id, UserDto userDto) {
        return userDao.updateUser(id, UserMapper.toUser(userDto));
    }

    public User deleteUser(long id) {
        return userDao.deleteUser(id);
    }
}
