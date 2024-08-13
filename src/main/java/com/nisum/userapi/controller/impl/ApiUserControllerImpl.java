package com.nisum.userapi.controller.impl;

import com.nisum.userapi.controller.ApiUserController;
import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.service.ApiUserService;
import com.nisum.userapi.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class ApiUserControllerImpl implements ApiUserController {

    private final ApiUserService apiUserService;
    private final LoginService loginService;

    @Override
    @GetMapping
    public ResponseEntity<List<ApiUserDto>> findAllUsers() {
        return ResponseEntity.ok(apiUserService.findAllUsers());
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApiUserDto> findUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(apiUserService.findUser(userId));
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiUserDto> createUser(@RequestBody ApiUserDto userRequest) {
        ApiUserDto userResponse = apiUserService.createUser(userRequest);
        ApiUserLoginDto loginResponse = loginService.login(new ApiUserLoginDto(userRequest.getEmail(), userRequest.getPassword()));
        userResponse.setLastLogin(loginResponse.getTimestamp());
        userResponse.setToken(loginResponse.getToken());
        userResponse.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
