package com.nisum.userapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1948")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiUserDto implements UserDetails {

    @Schema(name = "id", description = "Unique identification code for the user", example = "84c10e61-3bf7-4e83-bbb3-e1bc80e49fe6")
    private UUID id;

    @Schema(name = "name", description = "First and last name of the user", example = "Jack Johnson")
    private String name;

    @Schema(name = "email", description = "Email address of the user", example = "jack.johnson@nisum.com")
    private String email;

    @Schema(name = "password", description = "Password set by the user to authenticate", example = "aBcd&2024")
    private String password;

    @JsonProperty("created")
    @Schema(name = "created", description = "Timestamp when the user was created", example = "2024-08-13T20:04:08.108920231")
    private LocalDateTime createdAt;

    @JsonProperty("modified")
    @Schema(name = "modified", description = "Timestamp of the latest user update", example = "2024-08-13T20:04:08.108920231")
    private LocalDateTime lastUpdatedAt;

    @JsonProperty("last_login")
    @Schema(name = "last_login", description = "Timestamp of the latest login of the user", example = "2024-08-13T20:04:08.108920231")
    private LocalDateTime lastLogin;

    @JsonProperty("isactive")
    @Schema(name = "isactive", description = "Indicates whether if the user is active or not", example = "true")
    private Boolean isActive;

    private String token;

    @Schema(name = "phones", description = "List of phones of the user", example = "true")
    private List<ApiUserPhoneDto> phones;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isActive;
    }
}
