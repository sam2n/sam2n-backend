package com.sam2n.backend.controller;

import com.sam2n.backend.domain.User;
import com.sam2n.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsersV1() {
        log.debug("REST request to get all Users");

        List<User> allUsers = userService.getAll();
        return ResponseEntity.ok().body(allUsers);
    }
}
