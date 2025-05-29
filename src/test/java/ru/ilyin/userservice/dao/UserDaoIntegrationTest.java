package ru.ilyin.userservice.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ilyin.userservice.entity.User;
import ru.ilyin.userservice.util.HibernateUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@Testcontainers
public class UserDaoIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static UserDaoImpl userDao;

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

    @Test
    void testfindAll(){
        userDao.save(new User("Alex", "alex@example.com", 20));
        userDao.save(new User("Bob", "bob@example.com", 30));

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdate(){
        User user = userDao.save(new User("Charlie", "charlie@example.com", 40));
        user.setName("Charles");
        user.setAge(41);

        User user1 = userDao.update(user);
        assertEquals("Charles", user1.getName());
        assertEquals(41, user1.getAge());
    }

    @Test
    void testDelete(){
        User user = userDao.save(new User("Dave", "dave@example.com", 30));
        userDao.delete(user.getId());


        User deletedUser = userDao.findById(user.getId());
        assertNull(deletedUser);


    }

}
