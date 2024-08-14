package com.nisum.userapi.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nisum.userapi.BaseTest;
import com.nisum.userapi.auth.JwtUtil;
import com.nisum.userapi.auth.JwtUtilConfig;
import com.nisum.userapi.auth.JwtUtilParameters;
import com.nisum.userapi.config.ApiAuthenticationConfiguration;
import com.nisum.userapi.config.ApiSecurityConfiguration;
import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.exception.ApiException;
import com.nisum.userapi.service.ApiUserService;
import com.nisum.userapi.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiUserControllerImpl.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
class ApiUserControllerImplTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiUserService userService;

    @MockBean
    private LoginService loginService;

    @Test
    void whenCreatingNonExistingUserThenUserIsCreatedOK() throws Exception {
        ApiUserDto userRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});
        ApiUserDto userResponse = readFile(CREATE_USER_DTO_RESPONSE_PATH, new TypeReference<>() {});

        ApiUserLoginDto loginRequest = new ApiUserLoginDto(userRequest.getEmail(), userRequest.getPassword());
        ApiUserLoginDto loginResponse = readFile(LOGIN_DTO_RESPONSE_PATH, new TypeReference<>() {});

        when(userService.createUser(userRequest)).thenReturn(userResponse);
        when(loginService.login(loginRequest)).thenReturn(loginResponse);

        ApiUserDto serviceResponse = objectMapper.readValue(
                this.mockMvc
                        .perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {}
        );

        verify(userService).createUser(userRequest);
        verify(loginService).login(loginRequest);

        assertThat(serviceResponse).isEqualTo(userResponse);
    }

    @Test
    void whenCreatingAnExistingUserThenErrorResponseIsReceived() throws Exception {
        ApiUserDto userRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});

        when(userService.createUser(userRequest)).thenThrow(new ApiException(HttpStatus.CONFLICT, USER_ALREADY_EXISTS_MSG));

        this.mockMvc
                .perform(post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(USER_ALREADY_EXISTS_MSG));

        verify(userService).createUser(userRequest);
    }
}