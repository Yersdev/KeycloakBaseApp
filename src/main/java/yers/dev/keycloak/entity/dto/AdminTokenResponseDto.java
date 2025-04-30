package yers.dev.keycloak.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Рецепт для разбора ответа Keycloak по client_credentials:
 * {
 *   "access_token":"…",
 *   "expires_in":3600,
 *   "refresh_expires_in":0,
 *   "token_type":"Bearer",
 *   "scope":"…"
 * }
 */
@Data
@NoArgsConstructor
public class AdminTokenResponseDto {
    /** Из поля "access_token" */
    @JsonProperty("access_token")
    private String accessToken;
    /** Из поля "token_type" */
    @JsonProperty("token_type")
    private String tokenType;
    /** Из поля "expires_in" */
    @JsonProperty("expires_in")
    private int expiresIn;
}
