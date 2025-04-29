package yers.dev.keycloak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yers.dev.keycloak.entity.Users;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByKeycloakId(String keycloakId);
    Optional<Users> findByUsername(String username);
}
