package ru.ilyin.userservice.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ilyin.userservice.entity.User;
import ru.ilyin.userservice.util.HibernateUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ilyin.userservice.TestContainersConfig.postgreSQLContainer;


@Testcontainers
public class UserDaoIntegrationTest {
    private static UserDao userDao;

    @BeforeAll
    static void beforeAll(){
        System.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
        System.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());

        userDao = new UserDaoImpl();
    }


    @BeforeEach
    void setUp(){
        try (var session = HibernateUtil.getSessionFactory().openSession()){
            var transaction = session.beginTransaction();
            session.createNativeQuery("Delete from User").executeUpdate();
            transaction.commit();
        }
    }

    @AfterAll
    static void afterAll(){
        HibernateUtil.shutdown();
    }
    @Test
    void testSaveAndFindById() {
        User user = new User("John", "john@example.com", 30);
        User savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());

        User foundUser = userDao.findById(savedUser.getId());
        assertNotNull(foundUser.getId());
        assertEquals(savedUser.getId(),foundUser.getId());
        assertEquals("John",foundUser.getName());
    }


}
