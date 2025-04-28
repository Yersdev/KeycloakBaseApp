package yers.dev.keycloak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yers.dev.keycloak.dto.AuthRequest;
import yers.dev.keycloak.dto.RegistrationRequest;
import yers.dev.keycloak.service.AuthService;
import yers.dev.keycloak.service.KeycloakUserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakUserService keycloakUserService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRequest req) {
        // передаём весь объект, чтобы не лезть в параметры
        keycloakUserService.registerUser(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req.getUsername(), req.getPassword()));
    }
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refresh(@RequestBody Map<String,String> body) {
        return ResponseEntity.ok(authService.refresh(body.get("refreshToken")));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String,String> body) {
        authService.logout(body.get("refreshToken"));
        return ResponseEntity.ok().build();
    }
}
