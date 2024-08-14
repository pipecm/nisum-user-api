package com.nisum.userapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nisum.userapi.BaseTest;
import com.nisum.userapi.auth.JwtUtil;
import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.entity.ApiUser;
import com.nisum.userapi.entity.ApiUserLogin;
import com.nisum.userapi.exception.ApiException;
import com.nisum.userapi.mapper.ApiUserMapper;
import com.nisum.userapi.repository.ApiUserLoginRepository;
import com.nisum.userapi.repository.ApiUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest extends BaseTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @InjectMocks
    private ApiUserMapper userMapper = spy(new ApiUserMapper(passwordEncoder));

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ApiUserLoginRepository loginRepository;

    @Mock
    private ApiUserRepository userRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void whenLoginWithCorrectCredentialsThenLoginOK() throws Exception {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);
        UsernamePasswordAuthenticationToken beforeAuth = new UsernamePasswordAuthenticationToken(EMAIL, DECODED_PASSWORD);
        UsernamePasswordAuthenticationToken afterAuth = spy(beforeAuth);

        ApiUser user = readFile(CREATE_USER_ENTITY_RESPONSE_PATH, new TypeReference<>() {});
        ApiUserLogin login = new ApiUserLogin(user, LOGIN_TOKEN);

        doReturn(EMAIL).when(afterAuth).getName();

        when(passwordEncoder.encode(DECODED_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(authenticationManager.authenticate(beforeAuth)).thenReturn(afterAuth);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(loginRepository.save(any(ApiUserLogin.class))).thenReturn(login);
        when(jwtUtil.createToken(any(ApiUserDto.class))).thenReturn(LOGIN_TOKEN);
        when(jwtUtil.getExpiration(LOGIN_TOKEN)).thenReturn(TOKEN_EXPIRATION);

        ApiUserLoginDto loginResponse = loginService.login(loginRequest);

        verify(authenticationManager).authenticate(beforeAuth);
        verify(userRepository).findByEmail(EMAIL);
        verify(loginRepository).save(any(ApiUserLogin.class));
        verify(jwtUtil).createToken(any(ApiUserDto.class));
        verify(jwtUtil).getExpiration(LOGIN_TOKEN);

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getUsername()).isEqualTo(EMAIL);
        assertThat(loginResponse.getToken()).isEqualTo(LOGIN_TOKEN);
        assertThat(loginResponse.getExpiration()).isEqualTo(LDT_TOKEN_EXPIRATION);
    }

    @Test
    void whenLoginWithWrongCredentialsThenLoginFails() {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);
        UsernamePasswordAuthenticationToken beforeAuth = new UsernamePasswordAuthenticationToken(EMAIL, DECODED_PASSWORD);

        when(authenticationManager.authenticate(beforeAuth)).thenThrow(new BadCredentialsException(INVALID_LOGIN_MSG));

        try {
            loginService.login(loginRequest);
            fail(NO_EXCEPTION_THROWN);
        } catch (ApiException apiException) {
            assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(apiException.getMessage()).isEqualTo(INVALID_LOGIN_MSG);
        }

        verify(authenticationManager).authenticate(beforeAuth);
    }

    @Test
    void whenUserNotFoundThenLoginFails() {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);
        UsernamePasswordAuthenticationToken beforeAuth = new UsernamePasswordAuthenticationToken(EMAIL, DECODED_PASSWORD);
        UsernamePasswordAuthenticationToken afterAuth = spy(beforeAuth);
        doReturn(EMAIL).when(afterAuth).getName();

        when(authenticationManager.authenticate(beforeAuth)).thenReturn(afterAuth);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        try {
            loginService.login(loginRequest);
            fail(NO_EXCEPTION_THROWN);
        } catch (ApiException apiException) {
            assertThat(apiException.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(apiException.getMessage()).isEqualTo(USER_NOT_FOUND_MSG);
        }

        verify(authenticationManager).authenticate(beforeAuth);
        verify(userRepository).findByEmail(EMAIL);
    }
}