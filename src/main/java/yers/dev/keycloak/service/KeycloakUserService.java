package yers.dev.keycloak.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import yers.dev.keycloak.entity.dto.AuthRequest;
import yers.dev.keycloak.util.KeycloakHttpUtil;
import java.util.List;
import java.util.Map;
/**
 * Сервис для управления пользователями в Keycloak и локальной базе.
 * Обеспечивает регистрацию, обновление и получение сервисного токена.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {

    private final WebClient.Builder webClientBuilder;

    /**
     * Утилита для HTTP-запросов к Keycloak.
     */
    private final KeycloakHttpUtil keycloakHttpUtil;

    /**
     * Сервис для управления локальными пользователями.
     */
    private final UsersService usersService;

    /**
     * Сервис аутентификации.
     */
    private final AuthService authService;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    /**
     * Получает сервисный access_token от Keycloak по client_credentials.
     *
     * @return access_token в виде строки
     */
    public String getAdminAccessToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", adminClientId);
        form.add("client_secret", adminClientSecret);

        return (String) keycloakHttpUtil.postForm(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect",
                "/token",
                form
        ).get("access_token");
    }

    /**
     * Регистрирует пользователя в Keycloak и локальной базе.
     * <p>
     * Использует admin-токен для создания пользователя в Keycloak,
     * затем вызывает локальный {@code UsersService} для создания пользователя.
     *
     * @param req данные пользователя для регистрации
     * @return объект с токенами после успешной авторизации
     */
    @Transactional
    public Map<String, Object> registerUser(AuthRequest req) {
        String token = getAdminAccessToken();
        Map<String, Object> payload = Map.of(
                "firstName", req.getFirstName(),
                "lastName", req.getLastName(),
                "email", req.getEmail(),
                "emailVerified", true,
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", req.getPassword(),
                        "temporary", false
                ))
        );

        // Используем новый метод для регистрации пользователя
        String keycloakId = keycloakHttpUtil.registerUser(keycloakUrl + "/admin/realms/" + realm, token, payload);

        // Дальше можно продолжить регистрацию в вашей системе
        usersService.registerUser(req, keycloakId);

        return authService.login(req);
    }



    /**
     * Обновляет данные пользователя в Keycloak и локальной базе.
     *
     * @param keycloakId UUID пользователя в Keycloak
     * @param req обновлённые данные пользователя
     */
    @Transactional
    public void updateUser(String keycloakId, AuthRequest req) {
        String token = getAdminAccessToken();

        Map<String, Object> payload = Map.of(
                "firstName", req.getFirstName(),
                "lastName", req.getLastName(),
                "email", req.getEmail(),
                "emailVerified", true
        );

        keycloakHttpUtil.putJson(
                keycloakUrl + "/admin/realms/" + realm,
                "/users/" + keycloakId,
                token,
                payload
        );

        usersService.updateUser(req, keycloakId);
    }
}