package com.nisum.userapi.mapper;

import com.nisum.userapi.dto.ApiUserPhoneDto;
import com.nisum.userapi.entity.ApiUserPhone;
import org.springframework.stereotype.Component;

@Component
public class ApiUserPhoneMapper {

    public ApiUserPhone map(ApiUserPhoneDto apiUserPhoneDto) {
        return ApiUserPhone.builder()
                .id(apiUserPhoneDto.getId())
                .countryCode(apiUserPhoneDto.getCountryCode())
                .cityCode(apiUserPhoneDto.getCityCode())
                .phoneNumber(apiUserPhoneDto.getPhoneNumber())
                .build();
    }

    public ApiUserPhoneDto map(ApiUserPhone apiUserPhone) {
        return ApiUserPhoneDto.builder()
                //.id(apiUserPhone.getId())
                .countryCode(apiUserPhone.getCountryCode())
                .cityCode(apiUserPhone.getCityCode())
                .phoneNumber(apiUserPhone.getPhoneNumber())
                .build();
    }
}
