package com.nisum.userapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nisum.userapi.BaseTest;
import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.dto.ApiUserPhoneDto;
import com.nisum.userapi.entity.ApiUser;
import com.nisum.userapi.entity.ApiUserPhone;
import com.nisum.userapi.exception.ApiException;
import com.nisum.userapi.mapper.ApiUserMapper;
import com.nisum.userapi.mapper.ApiUserPhoneMapper;
import com.nisum.userapi.repository.ApiUserLoginRepository;
import com.nisum.userapi.repository.ApiUserPhoneRepository;
import com.nisum.userapi.repository.ApiUserRepository;
import com.nisum.userapi.util.ApiUserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUserServiceImplTest extends BaseTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Mock
    private ApiUserPhoneRepository phoneRepository;

    @Mock
    private ApiUserRepository userRepository;

    @Mock
    private ApiUserPhoneMapper phoneMapper;

    @Mock
    private ApiUserUtils apiUserUtils;

    @InjectMocks
    private ApiUserMapper userMapper = spy(new ApiUserMapper(passwordEncoder));

    @InjectMocks
    private ApiUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(apiUserUtils, PASSWORD_REGEX_FIELD, PASSWORD_REGEX);
    }

    @Test
    void whenCreatingNonExistingUserThenUserCreatedSuccessfully() throws Exception {
        ApiUserDto dtoRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});
        ApiUserDto dtoResponse = readFile(CREATE_USER_DTO_RESPONSE_PATH, new TypeReference<>() {});

        ApiUser userAfterSaving = readFile(CREATE_USER_ENTITY_RESPONSE_PATH, new TypeReference<>() {});
        ApiUserPhone phoneAfterSaving = readFile(CREATE_USER_PHONE_ENTITY_RESPONSE_PATH, new TypeReference<>() {});

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(phoneMapper.map(any(ApiUserPhoneDto.class))).thenCallRealMethod();
        when(phoneMapper.map(any(ApiUserPhone.class))).thenCallRealMethod();
        when(userRepository.save(any(ApiUser.class))).thenReturn(userAfterSaving);
        when(passwordEncoder.encode(DECODED_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(phoneRepository.save(any(ApiUserPhone.class))).thenReturn(phoneAfterSaving);

        doCallRealMethod().when(apiUserUtils).validate(any(ApiUserDto.class));

        ApiUserDto created = userService.createUser(dtoRequest);

        verify(userRepository).findByEmail(EMAIL);
        verify(phoneMapper).map(any(ApiUserPhoneDto.class));
        verify(phoneMapper).map(any(ApiUserPhone.class));
        verify(userRepository).save(any(ApiUser.class));
        verify(passwordEncoder).encode(DECODED_PASSWORD);
        verify(phoneRepository).save(any(ApiUserPhone.class));

        assertThat(created).isEqualTo(dtoResponse);
    }

    @Test
    void whenCreatingUserWithExistingEmailThenErrorIsThrown() throws Exception {
        ApiUserDto dtoRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});
        ApiUser userFound = readFile(CREATE_USER_ENTITY_RESPONSE_PATH, new TypeReference<>() {});

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userFound));

        try {
            userService.createUser(dtoRequest);
            fail(NO_EXCEPTION_THROWN);
        } catch (ApiException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(e.getMessage()).isEqualTo(USER_ALREADY_EXISTS_MSG);
        }

        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void whenLoadingByUsernameThenUserRetrievedSuccessfully() throws Exception {
        ApiUser userRetrieved = readFile(CREATE_USER_ENTITY_RESPONSE_PATH, new TypeReference<>() {});
        ApiUserDto dtoResponse = readFile(CREATE_USER_DTO_RESPONSE_PATH, new TypeReference<>() {});
        dtoResponse.setPhones(null);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userRetrieved));

        UserDetails userLoaded = userService.loadUserByUsername(EMAIL);

        verify(userRepository).findByEmail(EMAIL);

        assertThat(userLoaded).isEqualTo(dtoResponse);
    }

    @Test
    void whenLoadingByNonExistingUsernameThenErrorIsThrown() {

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        try {
            userService.loadUserByUsername(EMAIL);
            fail(NO_EXCEPTION_THROWN);
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo(USER_NOT_FOUND_MSG);
        }

        verify(userRepository).findByEmail(EMAIL);
    }
}