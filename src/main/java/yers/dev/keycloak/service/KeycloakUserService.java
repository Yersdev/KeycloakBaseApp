package yers.dev.keycloak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import yers.dev.keycloak.dto.AuthRequest;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final WebClient.Builder webClientBuilder;

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

    /** Регистрируем пользователя вместе с именем/фамилией */
    public void registerUser(AuthRequest req) {
        String token = getAdminAccessToken();

        Map<String,Object> payload = Map.of(
                "username",       req.getUsername(),
                "firstName",      req.getFirstName(),
                "lastName",       req.getLastName(),
                "email",          req.getEmail(),          // ← теперь обязательно
                "emailVerified",  true,                    // ← чтобы сразу было верифицировано
                "enabled",        true,
                "credentials",    List.of(Map.of(
                        "type",      "password",
                        "value",     req.getPassword(),
                        "temporary", false
                ))
        );

        webClientBuilder
                .baseUrl(keycloakUrl + "/admin/realms/" + realm)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post().uri("/users")
                .bodyValue(payload)
                .retrieve()
                .onStatus(status -> status != HttpStatus.CREATED, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Create user error: " + resp.statusCode() + " / " + body)))
                )
                .toBodilessEntity()
                .block();
    }

}
