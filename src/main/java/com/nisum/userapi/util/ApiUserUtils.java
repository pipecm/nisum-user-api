package com.nisum.userapi.util;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.exception.ApiException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public final class ApiUserUtils {

    private static final String NUMBERS_REGEX = "\\d+";
    private static final String EMPTY_NAME_MSG = "Name cannot be empty or null";
    private static final String EMPTY_EMAIL_MSG = "Email cannot be empty or null";
    private static final String EMPTY_PASSWORD_MSG = "Password cannot be empty or null";
    private static final String INVALID_EMAIL_MSG = "Invalid email address";
    private static final String EMPTY_OR_INVALID_PHONE_MSG = "Empty or invalid phone provided";

    private ApiUserUtils() {}

    public static void validate(ApiUserDto apiUserDto) {
        if (ObjectUtils.isEmpty(apiUserDto.getName())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, EMPTY_NAME_MSG);
        }

        if (ObjectUtils.isEmpty(apiUserDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, EMPTY_EMAIL_MSG);
        }

        if (ObjectUtils.isEmpty(apiUserDto.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, EMPTY_PASSWORD_MSG);
        }

        if (!EmailValidator.getInstance().isValid(apiUserDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_EMAIL_MSG);
        }

        if (apiUserDto.getPhones()
                .stream()
                .flatMap(phone -> Stream.of(phone.getPhoneNumber(), phone.getCityCode(), phone.getCountryCode()))
                .anyMatch(phoneData -> ObjectUtils.isEmpty(phoneData) || !phoneData.matches(NUMBERS_REGEX))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, EMPTY_OR_INVALID_PHONE_MSG);
        }

        LocalDateTime currentTimestamp = LocalDateTime.now();
        apiUserDto.setIsActive(true);
        apiUserDto.setCreatedAt(currentTimestamp);
        apiUserDto.setLastUpdatedAt(currentTimestamp);
    }
}
