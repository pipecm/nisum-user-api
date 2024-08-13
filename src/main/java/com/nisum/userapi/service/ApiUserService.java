package com.nisum.userapi.service;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.entity.ApiUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface ApiUserService extends UserDetailsService {
    List<ApiUserDto> findAllUsers();
    ApiUserDto findUser(UUID userId);
    ApiUser findByEmail(String email);
    ApiUserDto createUser(ApiUserDto apiUserDto);
}
