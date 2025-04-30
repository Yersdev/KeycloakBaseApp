package yers.dev.keycloak.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import yers.dev.keycloak.entity.dto.AuthRequest;
import yers.dev.keycloak.util.KeycloakAdminTokenProvider;
import yers.dev.keycloak.util.KeycloakRoleProvider;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {

    private final WebClient.Builder webClientBuilder;
    private final UsersService usersService;
    private final AuthService authService;
    private final KeycloakAdminTokenProvider adminTokenProvider;
    private final KeycloakRoleProvider roleProvider;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.admin.client-id}")
    private String adminClientId;
    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    /** Получаем сервисный токен по client_credentials */
    public String getAdminAccessToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id",   adminClientId);
        form.add("client_secret", adminClientSecret);

        Map<String, Object> resp = webClientBuilder
                .baseUrl(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .post().uri("/token")
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return (String) resp.get("access_token");
    }
    @Transactional
    public Map<String,Object> registerUser(AuthRequest req) {
        String token = getAdminAccessToken();
        Map<String,Object> payload = Map.of(
                "firstName",     req.getFirstName(),
                "lastName",      req.getLastName(),
                "email",         req.getEmail(),
                "emailVerified", true,
                "enabled",       true,
                "credentials", List.of(Map.of(
                        "type",      "password",
                        "value",     req.getPassword(),
                        "temporary", false
                ))
        );

        try {
            // вместо retrieve() используем exchangeToMono, чтобы точно обработать статус и тело
            var response = webClientBuilder
                    .baseUrl(keycloakUrl + "/admin/realms/" + realm)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .post().uri("/users")
                    .bodyValue(payload)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().equals(HttpStatus.CREATED)) {
                            return clientResponse.toBodilessEntity();
                        } else {
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Create user failed: HTTP " +
                                                    clientResponse.statusCode() +
                                                    " / body: " + body
                                    )));
                        }
                    })
                    .block();

            // здесь можно извлечь Location и keycloakId если нужно
            String location = response.getHeaders().getLocation().toString();
            String keycloakId = location.substring(location.lastIndexOf('/') + 1);

            usersService.registerUser(req, keycloakId);


        } catch (WebClientResponseException e) {
            // явная логика логирования, чтобы увидеть тело ошибки
            log.error("Keycloak returned {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;  // или бросить своё исключение с более понятным сообщением
        }

        Map<String,Object> resp = authService.login(req);
        return resp;
    }





    @Transactional
    public void updateUser(String keycloakId, AuthRequest req) {
        String token = getAdminAccessToken();

        // 1) PUT в Keycloak
        Map<String, Object> payload = Map.of(
                "firstName", req.getFirstName(),
                "lastName", req.getLastName(),
                "email", req.getEmail(),
                "emailVerified", true
        );

        webClientBuilder
                .baseUrl(keycloakUrl + "/admin/realms/" + realm)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .put().uri("/users/{id}", keycloakId)
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Keycloak update failed: " + resp.statusCode() + " / " + body
                                )))
                )
                .toBodilessEntity()
                .block();

        usersService.updateUser(req, keycloakId);
    }
}