package yers.dev.keycloak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yers.dev.keycloak.constant.HttpStatusConstants;
import yers.dev.keycloak.entity.dto.AuthRequest;
import yers.dev.keycloak.entity.dto.ErrorResponseDto;
import yers.dev.keycloak.entity.dto.UsersDto;
import yers.dev.keycloak.service.KeycloakUserService;
import yers.dev.keycloak.service.UsersService;
import java.util.List;

@Tag(
        name = "REST API for check Auth of user",
        description = "REST APIs in EazyBank to FETCH customer details"
)
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Validated
public class UserController {
    private final UsersService usersService;
    private final KeycloakUserService keycloakUserService;

    @Operation(
            summary = "Fetch all Users",
            description = "REST API to fetch all Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/all")
    public ResponseEntity<List<UsersDto>> getAll(){
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(usersService.getAll());
    }

    @Operation(
            summary = "Fetch user by jwt",
            description = "REST API to fetch Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/me")
    public ResponseEntity<UsersDto> getMe(
            @AuthenticationPrincipal Jwt jwt        // ← вот сюда Spring подставит ваш JWT
    ) {
        String keycloakId = jwt.getSubject();       // claim "sub" — UUID пользователя в Keycloak
        return ResponseEntity
                .status(HttpStatusConstants.OK)
                .body(usersService.getMe(keycloakId));
    }

    @Operation(
            summary = "Update user by jwt",
            description = "REST API to update Account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PutMapping("/me")
    public ResponseEntity<HttpStatus> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AuthRequest reqBody
    ) {
        keycloakUserService.updateUser(jwt.getSubject(), reqBody);
        return ResponseEntity.noContent().build();
    }
}
