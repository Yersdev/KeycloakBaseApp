package yers.dev.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ Keycloak при grant_type=client_credentials
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminTokenResponseDto {

    /** JWT-токен для admin-операций */
    @JsonProperty("access_token")
    private String accessToken;

    /** Время жизни токена в секундах */
    @JsonProperty("expires_in")
    private int expiresIn;

    /** Время жизни refresh-токена, обычно не используется для client_credentials */
    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    /** Тип токена, обычно «Bearer» */
    @JsonProperty("token_type")
    private String tokenType;

    /** Список скоупов, если задавались */
    @JsonProperty("scope")
    private String scope;
}
