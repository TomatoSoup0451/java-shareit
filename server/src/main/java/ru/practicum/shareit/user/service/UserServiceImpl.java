package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + id + " not found")));
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("User with id = {} added", user.getId());
        return user;
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        User oldUser = UserMapper.toUser(getUser(id));
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUser.setEmail(userDto.getEmail());
        }
        User updatedUser = userRepository.save(oldUser);
        log.info("User with id = {} updated", id);
        return updatedUser;
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
