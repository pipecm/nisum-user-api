package com.nisum.userapi.controller.impl;

import com.nisum.userapi.controller.LoginController;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginControllerImpl implements LoginController {

    private final LoginService loginService;

    @Override
    @PostMapping
    public ResponseEntity<ApiUserLoginDto> login(@RequestBody ApiUserLoginDto loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }
}
