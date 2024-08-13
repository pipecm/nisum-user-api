package com.nisum.userapi.controller;

import com.nisum.userapi.dto.ApiUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ApiUserController {
    ResponseEntity<List<ApiUserDto>> findAllUsers();
    ResponseEntity<ApiUserDto> findUser(UUID userId);
    ResponseEntity<ApiUserDto> createUser(ApiUserDto apiUserDto);
}
