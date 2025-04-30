package yers.dev.keycloak.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "AuthRequest", description = "AuthRequest")
public class AuthRequest {

    @Schema(
            description = "password", example = "123456789"
    )
    private String password;
    @Schema(
            description = "First name", example = "John"
    )
    private String firstName;
    @Schema(
            description = "Last name", example = "Doe"
    )
    private String lastName;
    @Schema(
            description = "Email", example = "lY6m6@example.com"
    )
    private String email;
}
