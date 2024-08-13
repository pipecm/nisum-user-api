package com.nisum.userapi.mapper;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.entity.ApiUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApiUserMapper {

    private final PasswordEncoder passwordEncoder;

    public ApiUser map(ApiUserDto apiUserDto) {
        return ApiUser.builder()
                .id(apiUserDto.getId())
                .name(apiUserDto.getName())
                .email(apiUserDto.getEmail())
                .password(passwordEncoder.encode(apiUserDto.getPassword()))
                .createdAt(apiUserDto.getCreatedAt())
                .lastUpdatedAt(apiUserDto.getLastUpdatedAt())
                .active(apiUserDto.getIsActive())
                .build();
    }

    public ApiUserDto map(ApiUser apiUser) {
        return ApiUserDto.builder()
                .id(apiUser.getId())
                .name(apiUser.getName())
                .password(apiUser.getPassword())
                .email(apiUser.getEmail())
                .createdAt(apiUser.getCreatedAt())
                .lastUpdatedAt(apiUser.getLastUpdatedAt())
                .lastLogin(apiUser.getLastLogin())
                .token(apiUser.getLastToken())
                .isActive(apiUser.isActive())
                .build();
    }
}
