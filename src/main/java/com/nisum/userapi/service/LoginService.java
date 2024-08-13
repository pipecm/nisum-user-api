package com.nisum.userapi.service;

import com.nisum.userapi.dto.ApiUserLoginDto;

public interface LoginService {
    ApiUserLoginDto login(ApiUserLoginDto loginRequest);
}
