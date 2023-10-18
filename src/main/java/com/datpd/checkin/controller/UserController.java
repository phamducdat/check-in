package com.datpd.checkin.controller;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> checkInByUserId(@PathVariable long userId) {
        try {
            userService.checkInByUserId(userId);
            return ResponseEntity.ok("Check-in successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
