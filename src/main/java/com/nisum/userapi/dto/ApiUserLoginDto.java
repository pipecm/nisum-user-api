package com.nisum.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUserLoginDto {
    private String username;
    private String password;
    private String token;
    private LocalDateTime timestamp;
    private LocalDateTime expiration;

    public ApiUserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
