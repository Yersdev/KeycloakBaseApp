package yers.dev.keycloak.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Рецепт для разбора JSON-ответа Keycloak GET /admin/realms/{realm}/roles/{roleName}:
 * {
 *   "id":"1cbfcb83-c49d-4826-a6fb-2b9ce96b5510",
 *   "name":"USER",
 *   "description":"Regular user",
 *   …
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakRoleResponseDto {
    /** uuid роли в Keycloak */
    private String id;
    /** имя роли, например "USER" */
    private String name;
}
