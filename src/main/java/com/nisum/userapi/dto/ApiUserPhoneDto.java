package com.nisum.userapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiUserPhoneDto {

    private UUID id;

    @JsonProperty("countrycode")
    private String countryCode;

    @JsonProperty("citycode")
    private String cityCode;

    @JsonProperty("number")
    private String phoneNumber;
}
