package ru.ilyin.userservice.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ilyin.userservice.model.User;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DataJpaTest
@Testcontainers
@Sql(scripts = "/test-data.sql")
public class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");


    @Autowired
    private UserRepository userRepository;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }




    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists(){
        List<User> user = userRepository.findByEmail("test@mail.ru");
        assertEquals("test@mail.ru", user.get(0).getEmail());
        assertEquals("Test User", user.get(0).getName());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists(){
        boolean exists = userRepository.existsByEmail("test@mail.ru");
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist(){
        boolean exists = userRepository.existsByEmail("nonexistent@mail.ru");
        assertFalse(exists);
    }
}
