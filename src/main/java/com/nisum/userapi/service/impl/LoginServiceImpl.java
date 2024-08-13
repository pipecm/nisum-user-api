package com.nisum.userapi.service.impl;

import com.nisum.userapi.auth.JwtUtil;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.entity.ApiUser;
import com.nisum.userapi.entity.ApiUserLogin;
import com.nisum.userapi.exception.ApiException;
import com.nisum.userapi.mapper.ApiUserMapper;
import com.nisum.userapi.repository.ApiUserLoginRepository;
import com.nisum.userapi.repository.ApiUserRepository;
import com.nisum.userapi.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String INVALID_LOGIN_MSG = "Invalid email and/or password";

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ApiUserLoginRepository loginRepository;
    private final ApiUserRepository userRepository;
    private final ApiUserMapper userMapper;

    @Override
    public ApiUserLoginDto login(ApiUserLoginDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            ApiUser user = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

            ApiUserLogin apiUserLogin = loginRepository.save(new ApiUserLogin(user, jwtUtil.createToken(userMapper.map(user))));

            return ApiUserLoginDto.builder()
                    .username(user.getEmail())
                    .token(apiUserLogin.getToken())
                    .timestamp(apiUserLogin.getTimestamp())
                    .expiration(jwtUtil.getExpiration(apiUserLogin.getToken()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

        } catch (IllegalArgumentException | BadCredentialsException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_LOGIN_MSG);
        }
    }
}
