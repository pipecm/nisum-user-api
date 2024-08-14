package com.nisum.userapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public abstract class BaseTest {
    public static final String EMAIL = "fulano@nisum.com";
    public static final String DECODED_PASSWORD = "fd07s9fd76d";
    public static final String ENCODED_PASSWORD = "$2a$10$VtLnU/ZYARTQOThiocEuue3w6xLMi0Z/PO3KvSBwwDEWuaHN2tfq2";
    public static final String NO_EXCEPTION_THROWN = "No exception thrown";
    public static final String USER_ALREADY_EXISTS_MSG = "User already exists";
    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String CREATE_USER_DTO_REQUEST_PATH = "src/test/resources/requests/create_user_dto_request.json";
    public static final String CREATE_USER_DTO_RESPONSE_PATH = "src/test/resources/responses/create_user_dto_response.json";
    public static final String CREATE_USER_ENTITY_RESPONSE_PATH = "src/test/resources/responses/create_user_entity_response.json";
    public static final String CREATE_USER_PHONE_ENTITY_RESPONSE_PATH = "src/test/resources/responses/create_user_phone_entity_response.json";
    public static final String LOGIN_DTO_RESPONSE_PATH = "src/test/resources/responses/login_dto_response.json";
    public static final String LOGIN_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGFwcGx5ZGlnaXRhbC5jb20iLCJlbWFpbCI6InVzZXJAYXBwbHlkaWdpdGFsLmNvbSIsImV4cCI6MTkzMTYxNDMzMH0.BYuxQ3MGNpfGMAXxYFcELZgBVpHZgOUybvu28sThGQMnAh-YyrM5nGSn9XU0ked9XRoMJsKlxJTpmNSEz55RYg";
    public static final Date TOKEN_EXPIRATION = Date.from(Instant.now().plusSeconds(900L));
    public static final LocalDateTime LDT_TOKEN_EXPIRATION = TOKEN_EXPIRATION.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    public static final String INVALID_LOGIN_MSG = "Invalid email and/or password";
    public static final UUID LOGIN_ID = UUID.fromString("d568123c-0520-460f-84d9-1b127569978a");
    public static final String USERS_ENDPOINT = "/users";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String ERROR_MSG_JSON_PATH = "$.mensaje";
    public static final String BEARER_TOKEN = "Bearer %s";
    public static final String ISE_ERROR_MSG = "An error occurred";
    public static final String WRONG_EMAIL = "nonregistered@nisum.com";
    public static final String WRONG_PASSWORD = "nonregistered";
    public static final String INVALID_EMAIL_OR_PWD_MSG = "Invalid email and/or password";
    public static final String EMPTY_NAME_MSG = "Name cannot be empty or null";
    public static final String EMPTY_EMAIL_MSG = "Email cannot be empty or null";
    public static final String EMPTY_PASSWORD_MSG = "Password cannot be empty or null";
    public static final String INVALID_EMAIL_MSG = "Invalid email address";
    public static final String EMPTY_OR_INVALID_PHONE_MSG = "Empty or invalid phone provided";
    public static final String EMPTY_STRING = "";
    public static final String INVALID_EMAIL = "user@nisum";
    public static final String INVALID_PHONE = "ABC";

    protected static ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> T readFile(String filePath, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(new File(filePath), typeReference);
    }
}
