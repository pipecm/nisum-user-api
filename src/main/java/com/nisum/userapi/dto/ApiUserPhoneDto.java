package com.nisum.userapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "id", description = "Unique identification code for the phone number", example = "84c10e61-3bf7-4e83-bbb3-e1bc80e49fe6")
    private UUID id;

    @JsonProperty("countrycode")
    @Schema(name = "countrycode", description = "Country code of the phone", example = "56")
    private String countryCode;

    @JsonProperty("citycode")
    @Schema(name = "citycode", description = "City code of the phone", example = "9")
    private String cityCode;

    @JsonProperty("number")
    @Schema(name = "number", description = "Phone number of the user", example = "87452271")
    private String phoneNumber;
}
