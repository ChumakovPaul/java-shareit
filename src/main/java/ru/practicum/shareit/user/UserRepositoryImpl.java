package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.HashMap;
import java.util.List;

@Data
@Repository
public class UserRepositoryImpl implements UserRepository {

    private long userCounter = 1;
    private final HashMap<Long, User> userStorage;
    @Autowired
    private final UserMapper userMapper;


    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = userMapper.toUser(userCreateDto);
        emailValidation(user.getEmail());
        user.setId(userCounter);
        userCounter++;
        userStorage.put(user.getId(), user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto, Long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new DataAlreadyExistException("Такого пользователя не существует");
        }
        User user = userStorage.get(userId);
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            emailValidation(userMapper.toUser(userUpdateDto).getEmail());
            user.setEmail(userUpdateDto.getEmail());
        }
        userStorage.put(userId, user);
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.values().stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public UserDto getUser(Long userId) {
        return userMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.remove(userId);
    }

    public void emailValidation(String email) {
        if (userStorage.values().stream()
                .anyMatch(storageUser -> storageUser.getEmail().equals(email))) {
            throw new DataAlreadyExistException("Такой email уже существует");
        }
    }
}