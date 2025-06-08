package ru.ilyin.userservice.service;

import ru.ilyin.userservice.dto.UserRequestDto;
import ru.ilyin.userservice.dto.UserResponseDto;


import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id,UserRequestDto userRequestDto);

    void deleteUser(Long id);



//    User save(User user);
//    User findById(Long id);
//    List<User> findAll();
//    User update(User user);
//    void delete(Long id);
}
