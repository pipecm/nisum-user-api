package com.nisum.userapi.controller;

import com.nisum.userapi.dto.ApiUserLoginDto;
import com.nisum.userapi.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Login", description = "Manages the login process to the API")
public interface LoginController {
    @Operation(
            summary = "Login",
            description = "Makes the login process to the API for the specified credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"mensaje\":\"Invalid password\"}"))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"mensaje\":\"Internal Server Error\"}"))
                    })
    })
    ResponseEntity<ApiUserLoginDto> login(ApiUserLoginDto loginRequest);
}
