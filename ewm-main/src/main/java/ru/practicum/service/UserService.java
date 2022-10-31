package ru.practicum.service;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findUsers(List<Long> ids, int from, int size);

    UserDto create(NewUserRequest user);

    void delete(Long id);
}

