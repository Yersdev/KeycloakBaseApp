package yers.dev.keycloak.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    // при желании можно добавить email, если хотите сохранять:
    // private String email;
    private String email;

}
