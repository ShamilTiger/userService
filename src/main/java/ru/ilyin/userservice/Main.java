package ru.ilyin.userservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilyin.userservice.dao.UserDao;
import ru.ilyin.userservice.dao.UserDaoImpl;
import ru.ilyin.userservice.entity.User;
import ru.ilyin.userservice.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final UserDao USER_DAO = new UserDaoImpl();
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting User Service Application");

            boolean running = true;
            while (running) {
                printMenu();
                int choice = Integer.parseInt(SCANNER.nextLine());

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        findUserById();
                        break;
                    case 3:
                        findAllUsers();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred: ", e);
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
            SCANNER.close();
            LOGGER.info("Application shutdown");
        }
    }

    private static void printMenu() {
        System.out.println("\nUser Service Menu:");
        System.out.println("1. Create User");
        System.out.println("2. Find User by ID");
        System.out.println("3. Find All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser(){
        System.out.println("\nCreate New User");
        System.out.print("Enter name: ");
        String name = SCANNER.nextLine();

        System.out.print("Enter email (format: user@example.com): ");
        String email = SCANNER.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(SCANNER.nextLine());

        User user = new User(name, email, age);
        User savedUser = USER_DAO.save(user);
        System.out.println("User created successfully: " + savedUser);
        LOGGER.info("Created user: {}", savedUser);

    }

    private static void findUserById(){
        System.out.println("\nEnter user ID: ");
        Long id = Long.parseLong(SCANNER.nextLine());

        User user = USER_DAO.findById(id);
        if (user != null) {
            System.out.println("User found: " + user);
            LOGGER.info("Found user by id {}: {}", id, user);
        } else {
            System.out.println("User not found with ID: " + id);
            LOGGER.warn("User not found with id: {}", id);
        }
    }

    private static void findAllUsers(){
        List<User> users = USER_DAO.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            LOGGER.info("No users found in database");
        } else {
            System.out.println("\nList of Users:");
            users.forEach(System.out::println);
            LOGGER.info("Found {} users", users.size());
        }
    }

    private static void updateUser() {
        System.out.print("\nEnter user ID to update: ");
        Long id = Long.parseLong(SCANNER.nextLine());

        User user = USER_DAO.findById(id);
        if (user == null) {
            System.out.println("User not found with ID: " + id);
            LOGGER.warn("Attempt to update non-existent user with id: {}", id);
            return;
        }

        System.out.println("Current user details: " + user);
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = SCANNER.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        System.out.print("Enter new email (leave blank to keep current): ");
        String email = SCANNER.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Enter new age (leave blank to keep current): ");
        String ageInput = SCANNER.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        User updatedUser = USER_DAO.update(user);
        System.out.println("User updated successfully: " + updatedUser);
        LOGGER.info("Updated user: {}", updatedUser);
    }


    private static void deleteUser(){
        System.out.print("\nEnter user ID to delete: ");
        Long id = Long.parseLong(SCANNER.nextLine());

        User user = USER_DAO.findById(id);
        if (user == null) {
            System.out.println("User not found with ID: " + id);
            LOGGER.warn("Attempt to delete non-existent user with id: {}", id);
            return;
        }
        USER_DAO.delete(id);
        System.out.println("User deleted successfully with ID: " + id);
        LOGGER.info("Deleted user with id: {}", id);
    }
}