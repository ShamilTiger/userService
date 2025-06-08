package ru.ilyin.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ilyin.userservice.model.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);
    boolean existsByEmail (String email);

}
