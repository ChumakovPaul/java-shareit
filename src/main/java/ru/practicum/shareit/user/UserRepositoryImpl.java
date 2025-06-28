package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataAlreadyExistException;

import java.util.HashMap;
import java.util.List;

@Data
@Repository
public class UserRepositoryImpl implements UserRepository {

    private long userCounter = 1;
    private final HashMap<Long, User> userStorage;

    @Override
    public User createUser(User user) {
        emailValidation(user.getEmail());
        user.setId(userCounter);
        userCounter++;
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User updateUser, Long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new DataAlreadyExistException("Такого пользователя не существует");
        }
        User user = userStorage.get(userId);
        if (updateUser.getName() != null) {
            user.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            emailValidation(updateUser.getEmail());
            user.setEmail(updateUser.getEmail());
        }
        userStorage.put(userId, user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.values().stream().toList();
    }

    @Override
    public User getUser(Long userId) {
        return userStorage.get(userId);
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