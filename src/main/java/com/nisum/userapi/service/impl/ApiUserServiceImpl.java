package com.nisum.userapi.service.impl;

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
import com.nisum.userapi.service.ApiUserService;
import com.nisum.userapi.util.ApiUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {

    private static final String NOT_FOUND_MSG = "User not found";

    private final ApiUserRepository userRepository;
    private final ApiUserLoginRepository loginRepository;
    private final ApiUserPhoneRepository phoneRepository;
    private final ApiUserPhoneMapper phoneMapper;
    private final ApiUserMapper userMapper;

    @Override
    public List<ApiUserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::processExistingUser)
                .map(userMapper::map)
                .toList();
    }

    @Override
    public ApiUserDto findUser(UUID userId) {
        return userRepository.findById(userId)
                .map(this::processExistingUser)
                .map(userMapper::map)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NOT_FOUND_MSG));
    }

    @Override
    public ApiUserDto createUser(ApiUserDto apiUserDto) {
        ApiUserUtils.validate(apiUserDto);

        userRepository
                .findByEmail(apiUserDto.getEmail())
                .ifPresent(user -> { throw new ApiException(HttpStatus.CONFLICT, "User already exists"); });

        ApiUser savedApiUser = userRepository.save(userMapper.map(apiUserDto));

        List<ApiUserPhoneDto> savedPhones = apiUserDto.getPhones()
                .stream()
                .map(phoneMapper::map)
                .map(phone -> savePhone(savedApiUser, phone))
                .map(phoneMapper::map)
                .toList();

        ApiUserDto savedApiUserDto = userMapper.map(savedApiUser);
        savedApiUserDto.setPhones(savedPhones);

        return savedApiUserDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.map(findByEmail(username));
    }

    @Override
    public ApiUser findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_MSG));
    }

    private ApiUser processExistingUser(ApiUser apiUser) {
        apiUser.setLastLogin(loginRepository.findLastLogin(apiUser.getId()).orElse(null));
        return apiUser;
    }

    private ApiUserPhone savePhone(ApiUser apiUser, ApiUserPhone apiUserPhone) {
        apiUserPhone.setApiUser(apiUser);
        return phoneRepository.save(apiUserPhone);
    }
}
