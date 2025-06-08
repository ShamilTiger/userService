package ru.ilyin.userservice.model;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private int age;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

//    public User() {
//    }
//
//    public User(String name, String email, int age) {
//        this.name = name;
//        this.email = email;
//        this.age = age;
//        this.createdAt = LocalDateTime.now();
//    }

    public void setAge(@Min(value = 1, message = "Age must be at least 1") @Max(value = 120, message = "Age must be less than 120") int age) {
        this.age = age;
    }

    public void setEmail(@Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public void setName(@Size(min = 2, max = 50, message = "Name must be 2-50 characters") String name) {
        this.name = name;
    }


}
