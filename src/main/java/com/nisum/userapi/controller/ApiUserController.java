package com.nisum.userapi.controller;

import com.nisum.userapi.dto.ApiUserDto;
import com.nisum.userapi.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users", description = "Management of the users of the API")
public interface ApiUserController {
    @Operation(
            summary = "Create user",
            description = "Creates a new user in the API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"mensaje\":\"Invalid email\"}"))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"mensaje\":\"User already exists\"}"))
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
    ResponseEntity<ApiUserDto> createUser(ApiUserDto apiUserDto);
}
