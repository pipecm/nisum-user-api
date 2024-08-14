package com.nisum.userapi.service;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.entity.ApiUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ApiUserService extends UserDetailsService {
    ApiUser findByEmail(String email);
    ApiUserDto createUser(ApiUserDto apiUserDto);
}
