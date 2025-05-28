package ru.ilyin.userservice.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ilyin.userservice.dao.UserDao;
import ru.ilyin.userservice.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void createUser() {
        user = new User("Alex", "Alex@mail.ru", 20);

    }


    @Test
    void testGetUserById_IfExist(){
        when(userDao.findById(1L)).thenReturn(user);

        User result = userService.findById(1L);
        assertEquals("Alex", result.getName());

    }

    @Test
    void testGetUserById_IfNotExist(){
        when(userDao.findById(999L)).thenReturn(null);

        User result = userService.findById(1L);
        assertNull(null, result.getName());

    }

    @Test
    void createUserValidDataShouldReturnUser(){
        when(userDao.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);
        assertEquals("Alex",result.getName());
    }

//    @Test
//    void createUserInvalidEmailShouldThrowException(){
//        assertThrows(IllegalArgumentException.class, () -> {
//            user = createUser("Alex", "alex-email", 20);
//
//        })
//    }

    @Test
    void getAllUsersShouldReturnList(){
        List<User> users = List.of(
                new User("Alex", "alex@example.com", 20),
                new User("Bob", "bob@example.com", 30)
        );
        when(userDao.findAll()).thenReturn(users);

        assertEquals(2,users.size());

    }

    @Test
    void updateUserValidDataShouldUpdate(){
        User user1 = new User("Alex11", "alex11@mail.ru" , 26);
        when(userDao.findById(1L)).thenReturn(user);

        userService.update(user1);

        verify(userDao).update(argThat(user ->
                user.getName().equals("Alex11") &&
                user.getAge() == 26
        ));


    }

    @Test
    void deleteUserShouldCallDao(){
        userService.delete(1L);
        verify(userDao).delete(1L);
    }

}
