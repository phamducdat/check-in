package com.datpd.checkin.controller;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/check-in/{userId}")
    public UserDto checkInByUserId(@PathVariable long userId) {
        return userService.checkInByUserId(userId);
    }
}
