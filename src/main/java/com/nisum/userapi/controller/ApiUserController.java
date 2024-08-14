package com.nisum.userapi.controller;

import com.nisum.userapi.dto.ApiUserDto;
import org.springframework.http.ResponseEntity;

public interface ApiUserController {
    ResponseEntity<ApiUserDto> createUser(ApiUserDto apiUserDto);
}
