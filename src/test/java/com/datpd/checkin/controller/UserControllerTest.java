package com.datpd.checkin.controller;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;


    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setId(0L);
        user.setName("John");
        user.setTurn(0);
        userRepository.save(user);
    }

//    @Test
//    void shouldCheckInByUserId() throws Exception {
//        long userId = 0L;
//        UserDto mockUserDto = new UserDto();
//        mockUserDto.setName("John");
//        // set any other necessary fields in mockUserDto
//
//        when(userService.checkInByUserId(userId)).thenReturn(mockUserDto);
//
//        ResultActions result = mockMvc.perform(post("/users/check-in/{userId}", userId))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"id\":\"0\"}"))
//                .andExpect(content().json("{\"name\":\"John\"}"))
//                .andExpect(content().json("{\"turn\":\"2\"}"))
//                .andExpect(content().json("{\"enableCheckin\":\"false\"}"))
//                ;
//
//        verify(userService, times(1)).checkInByUserId(userId);
//    }
}
