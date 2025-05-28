package ru.ilyin.userservice.service;

import ru.ilyin.userservice.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);
    User findById(Long id);
    List<User> findAll();
    User update(User user);
    void delete(Long id);
}
