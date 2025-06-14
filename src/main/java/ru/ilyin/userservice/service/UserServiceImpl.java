package ru.ilyin.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilyin.userservice.dto.EventType;
import ru.ilyin.userservice.dto.UserEvent;
import ru.ilyin.userservice.dto.UserRequestDto;
import ru.ilyin.userservice.dto.UserResponseDto;
import ru.ilyin.userservice.model.User;
import ru.ilyin.userservice.exception.ResourceNotFoundException;
import ru.ilyin.userservice.repository.UserRepository;
import ru.ilyin.userservice.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new IllegalArgumentException("User with this email already exists");
        }
        User user = userMapper.toEntity(userRequestDto);
        User savedUser = userRepository.save(user);

        kafkaTemplate.send("user-events", new UserEvent(EventType.CREATED, savedUser.getEmail()));

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(!existingUser.getEmail().equals(userRequestDto.getEmail()) &&
        userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new IllegalArgumentException("Email is already in use by another user");
        }

        userMapper.updateEntity(userRequestDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("User not found"));

        userRepository.deleteById(id);

        kafkaTemplate.send("user-events", new UserEvent(EventType.DELETED, user.getEmail()));
    }


//    private final UserDao userDao;
//
////    public UserServiceImpl(UserDao userDao) {
////        this.userDao = userDao;
////    }
////
////    @Override
////    public User save(User user) {
////        return userDao.save(user);
////    }
////
////    @Override
////    public User findById(Long id) {
////        return userDao.findById(id);
////    }
////
////    @Override
////    public List<User> findAll() {
////        return userDao.findAll();
////    }
////
////    @Override
////    public User update(User user) {
////        return userDao.update(user);
////    }
////
////    @Override
////    public void delete(Long id) {
////        userDao.delete(id);
////    }
}
