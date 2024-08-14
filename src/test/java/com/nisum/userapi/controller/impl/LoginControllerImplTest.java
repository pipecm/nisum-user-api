package com.nisum.userapi.controller.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nisum.userapi.BaseTest;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoginControllerImpl.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
class LoginControllerImplTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    void whenLoginWithCorrectCredentialsThenSuccessfulResponseIsReturned() throws Exception {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);
        ApiUserLoginDto loginResponse = readFile(LOGIN_DTO_RESPONSE_PATH, new TypeReference<>() {});

        when(loginService.login(loginRequest)).thenReturn(loginResponse);

        ApiUserLoginDto serviceResponse = objectMapper.readValue(
                this.mockMvc
                        .perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {}
        );

        verify(loginService).login(loginRequest);

        assertThat(serviceResponse).isEqualTo(loginResponse);
    }

    @Test
    void whenLoginWithNonExistingUserThenErrorResponseIsReturned() throws Exception {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);

        when(loginService.login(loginRequest)).thenThrow(new UsernameNotFoundException(USER_NOT_FOUND_MSG));

        this.mockMvc
                .perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(USER_NOT_FOUND_MSG));

        verify(loginService).login(loginRequest);
    }

    @Test
    void whenOtherErrorOccurredWhenLoginThenErrorResponseIsReturned() throws Exception {
        ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);

        when(loginService.login(loginRequest)).thenThrow(new RuntimeException(ISE_ERROR_MSG));

        this.mockMvc
                .perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(ISE_ERROR_MSG));

        verify(loginService).login(loginRequest);
    }
}