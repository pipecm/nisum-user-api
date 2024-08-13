package com.nisum.userapi.controller;

import com.nisum.userapi.dto.ApiUserLoginDto;
import org.springframework.http.ResponseEntity;

public interface LoginController {
    ResponseEntity<ApiUserLoginDto> login(ApiUserLoginDto loginRequest);
}
