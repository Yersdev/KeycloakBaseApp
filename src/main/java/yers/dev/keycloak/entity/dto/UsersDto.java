package yers.dev.keycloak.entity.dto;

import lombok.Data;

@Data
public class UsersDto {

    private String username;

    private String firstName;

    private String lastName;

    private String email;
}
