package com.nisum.userapi.util;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.exception.ApiException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public final class ApiUserUtils {

    private static final String NUMBERS_REGEX = "[0-9]+";

    private ApiUserUtils() {}

    public static void validate(ApiUserDto apiUserDto) {
        if (ObjectUtils.isEmpty(apiUserDto.getName())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Name cannot be empty or null");
        }

        if (ObjectUtils.isEmpty(apiUserDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email cannot be empty or null");
        }

        if (ObjectUtils.isEmpty(apiUserDto.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password cannot be empty or null");
        }

        if (!EmailValidator.getInstance().isValid(apiUserDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }

        if (apiUserDto.getPhones()
                .stream()
                .flatMap(phone -> Stream.of(phone.getPhoneNumber(), phone.getCityCode(), phone.getCountryCode()))
                .anyMatch(phoneData -> ObjectUtils.isEmpty(phoneData) || !phoneData.matches(NUMBERS_REGEX))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid phone provided");
        }

        LocalDateTime currentTimestamp = LocalDateTime.now();
        apiUserDto.setIsActive(ObjectUtils.isEmpty(apiUserDto.getIsActive()) || apiUserDto.getIsActive());
        apiUserDto.setCreatedAt(ObjectUtils.isEmpty(apiUserDto.getCreatedAt()) ? currentTimestamp : apiUserDto.getCreatedAt());
        apiUserDto.setLastLogin(ObjectUtils.isEmpty(apiUserDto.getLastLogin()) ? currentTimestamp : apiUserDto.getLastLogin());
        apiUserDto.setLastUpdatedAt(currentTimestamp);
    }
}
