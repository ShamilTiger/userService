package ru.ilyin.userservice.controller;



import ru.ilyin.userservice.dto.UserRequestDto;
import ru.ilyin.userservice.dto.UserResponseDto;
import ru.ilyin.userservice.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;


    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception{
        UserRequestDto requestDto = new UserRequestDto("Test User", "test@mail.rum", 30);
        UserResponseDto responseDto = new UserResponseDto(1L,"Test User", "test@mail.ru", 30, LocalDateTime.now());

        given(userService.createUser(any(UserRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"));

    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("", "invalid-email", 0);


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.email").value("Email should be valid"))
                .andExpect(jsonPath("$.age").value("Age must be greater than 0"));

    }


    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(1L, "Test User", "test@mail.ru", 30, LocalDateTime.now());

        given(userService.getUserById(anyLong())).willReturn(responseDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(1L, "Test User", "test@mail.ru", 30, LocalDateTime.now());

        given(userService.getAllUsers()).willReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("Updated User", "updated@mail.ru", 35);
        UserResponseDto responseDto = new UserResponseDto(1L, "Updated User", "updated@mail.ru", 35, LocalDateTime.now());

        given(userService.updateUser(anyLong(), any(UserRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated User"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    
}
