package com.nisum.userapi;

import com.nisum.userapi.auth.JwtUtil;
import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.entity.ApiUser;
import com.nisum.userapi.mapper.ApiUserMapper;
import com.nisum.userapi.mapper.ApiUserPhoneMapper;
import com.nisum.userapi.repository.ApiUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NisumUserApiApplicationTests extends BaseTest {

	private enum BadRequestScenario { EMPTY_EMAIL, EMPTY_NAME, EMPTY_PASSWORD, EMPTY_PHONE, INVALID_EMAIL, INVALID_PHONE }

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ApiUserRepository userRepository;

	@Autowired
	private ApiUserMapper userMapper;

	@Autowired
	private ApiUserPhoneMapper phoneMapper;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		ApiUserDto persistedUser = readFile(CREATE_USER_DTO_RESPONSE_PATH, new TypeReference<>() {});
		userRepository.save(userMapper.map(persistedUser));
	}

	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	void whenCreatingNonExistingUserThenUserIsCreatedOK() throws Exception {
		ApiUserDto userRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});
		userRepository.deleteAll();

		ApiUserDto userResponse = objectMapper.readValue(
				this.mockMvc
						.perform(post(USERS_ENDPOINT)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
						.andExpect(status().isCreated())
						.andReturn()
						.getResponse()
						.getContentAsString(),
				new TypeReference<>() {});

		ApiUser userFromDb = userRepository.findByEmail(EMAIL).orElse(new ApiUser());

		assertThat(userResponse.getId()).isEqualTo(userFromDb.getId());
		assertThat(userResponse.getName()).isEqualTo(userFromDb.getName());
		assertThat(userResponse.getEmail()).isEqualTo(userFromDb.getEmail());
		assertThat(userResponse.getToken()).isNotNull();
		assertThat(userResponse.getIsActive()).isTrue();
		assertThat(userResponse.getPhones()).isEqualTo(userFromDb.getPhones().stream().map(phoneMapper::map).toList());
	}

	@Test
	void whenCreatingUserWithExistingEmailThenErrorIsReturned() throws Exception {
		ApiUserDto userRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});

		this.mockMvc
				.perform(post(USERS_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(USER_ALREADY_EXISTS_MSG));
	}

	@Test
	void whenLoginWithCorrectCredentialsThenSuccessfulResponseIsReturned() throws Exception {
		ApiUserLoginDto loginRequest = new ApiUserLoginDto(EMAIL, DECODED_PASSWORD);

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

		assertThat(serviceResponse.getUsername()).isEqualTo(EMAIL);
		assertThat(serviceResponse.getToken()).isNotNull();
		assertThat(serviceResponse.getExpiration()).isNotNull();
	}

	@Test
	void whenLoginWithNonExistingUserThenErrorResponseIsReturned() throws Exception {
		ApiUserLoginDto loginRequest = new ApiUserLoginDto(WRONG_EMAIL, WRONG_PASSWORD);

		this.mockMvc
				.perform(post(LOGIN_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(INVALID_EMAIL_OR_PWD_MSG));
	}

	@ParameterizedTest
	@EnumSource(BadRequestScenario.class)
	void whenSendingBadRequestThenErrorIsThrown(BadRequestScenario scenario) throws Exception {
		ApiUserDto userRequest = readFile(CREATE_USER_DTO_REQUEST_PATH, new TypeReference<>() {});
		String errorMessage = EMPTY_STRING;

		switch (scenario) {
			case EMPTY_NAME:
				userRequest.setName(null);
				errorMessage = EMPTY_NAME_MSG;
				break;
			case EMPTY_EMAIL:
				userRequest.setEmail(null);
				errorMessage = EMPTY_EMAIL_MSG;
				break;
			case EMPTY_PASSWORD:
				userRequest.setPassword(null);
				errorMessage = EMPTY_PASSWORD_MSG;
				break;
			case EMPTY_PHONE:
				userRequest.getPhones().getFirst().setPhoneNumber(null);
				errorMessage = EMPTY_OR_INVALID_PHONE_MSG;
				break;
			case INVALID_EMAIL:
				userRequest.setEmail(INVALID_EMAIL);
				errorMessage = INVALID_EMAIL_MSG;
				break;
			case INVALID_PHONE:
				userRequest.getPhones().getFirst().setPhoneNumber(INVALID_PHONE);
				errorMessage = EMPTY_OR_INVALID_PHONE_MSG;
				break;
		}

		this.mockMvc
				.perform(post(USERS_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath(ERROR_MSG_JSON_PATH).value(errorMessage));
	}
}
