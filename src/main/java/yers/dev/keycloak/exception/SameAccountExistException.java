package yers.dev.keycloak.exception;

public class SameAccountExistException extends RuntimeException {
    public SameAccountExistException(String email) {
        super(
                String.format("User with email %s already exist", email)
        );
    }
}
