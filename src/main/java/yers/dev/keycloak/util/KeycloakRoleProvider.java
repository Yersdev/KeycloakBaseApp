//package yers.dev.keycloak.util;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@RequiredArgsConstructor
//public class KeycloakRoleProvider {
//
//    private final WebClient keycloakWebClient;
//    private final SecurityProperties securityProperties;
//    private final KeycloakAdminTokenProvider adminTokenProvider;
//
//    private final Map<String, String> roleCache = new ConcurrentHashMap<>();
//
//    public KeycloakRoleResponseDto getRole(UserRole userRole) {
//        String roleName = userRole.name();
//
//        if (roleCache.containsKey(roleName)) {
//            return new KeycloakRoleResponseDto(roleCache.get(roleName), roleName);
//        }
//
//        String accessToken = adminTokenProvider.getAdminAccessToken().getAccessToken();
//
//        KeycloakRoleResponseDto roleResponseDto = keycloakWebClient.get()
//                .uri("/admin/realms/" + securityProperties.getRealm() + "/roles/" + roleName)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .retrieve()
//                .onStatus(
//                        status -> status.is4xxClientError() || status.is5xxServerError(),
//                        response -> response.bodyToMono(String.class)
//                                .map(body -> new RoleNotFoundException(
//                                        "Failed to fetch role \"" + roleName + "\": " + response.statusCode() + " - " + body))
//                )
//                .bodyToMono(KeycloakRoleResponseDto.class)
//                .block();
//
//        if (roleResponseDto == null || roleResponseDto.getId() == null) {
//            throw new RegistrationException("Role \"" + roleName + "\" not found in Keycloak");
//        }
//
//        roleCache.put(roleName, roleResponseDto.getId());
//
//        return roleResponseDto;
//    }
//
//    public void assignRole(KeycloakRoleResponseDto keycloakRoleResponseDto, String userId, String accessToken) {
//        keycloakWebClient.post()
//                .uri(securityProperties.getBaseUrl() + "/admin/realms/" + securityProperties.getRealm() + "/users/" + userId + "/role-mappings/realm")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(List.of(keycloakRoleResponseDto))
//                .retrieve()
//                .onStatus(
//                        status -> status.is4xxClientError() || status.is5xxServerError(),
//                        response -> response.bodyToMono(String.class)
//                                .map(body -> new RoleAssignmentException(
//                                        "Failed to assign role \"" + keycloakRoleResponseDto.getName() + "\" to userId " + userId +
//                                                ": " + response.statusCode() + " - " + body))
//                )
//                .toBodilessEntity()
//                .block();
//    }
//
//}