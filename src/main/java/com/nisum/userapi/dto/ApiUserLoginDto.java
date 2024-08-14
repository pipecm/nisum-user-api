package com.nisum.userapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiUserLoginDto {

    @Schema(name = "username", description = "Email of the user who wants to login", example = "john.jackson@nisum.com")
    private String username;

    @Schema(name = "password", description = "Password set by the user to authenticate", example = "aBcd&2024")
    private String password;

    @Schema(name = "token", description = "JWT token returned on success", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGFwcGx5ZGlnaXRhbC5jb20iLCJlbWFpbCI6InVzZXJAYXBwbHlkaWdpdGFsLmNvbSIsImV4cCI6MTkzMTYxNDMzMH0.BYuxQ3MGNpfGMAXxYFcELZgBVpHZgOUybvu28sThGQMnAh-YyrM5nGSn9XU0ked9XRoMJsKlxJTpmNSEz55RYg")
    private String token;

    @Schema(name = "timestamp", description = "Timestamp when the login is performed", example = "2024-08-13T20:04:08.108920231")
    private LocalDateTime timestamp;

    @Schema(name = "expiration", description = "Timestamp when the token will expire", example = "2024-08-13T20:04:08.108920231")
    private LocalDateTime expiration;

    public ApiUserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
