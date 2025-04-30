package yers.dev.keycloak.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO-класс для представления основных данных пользователя.
 * Используется для возврата информации о пользователе без чувствительных данных.
 */
@Schema(
        description = "Users data transfer object",
        name = "usersDto"
)
@Data
public class UsersDto {

    /** Имя пользователя */
    @Schema(
            description = "First name", example = "John"
    )
    private String firstName;

    /** Фамилия пользователя */
    @Schema(
            description = "Last name", example = "Doe"
    )
    private String lastName;

    /** Email пользователя */
    @Schema(
            description = "Email", example = "lY6m6@example.com"
    )
    private String email;
}
