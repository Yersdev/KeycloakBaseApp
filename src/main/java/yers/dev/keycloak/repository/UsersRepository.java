package yers.dev.keycloak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yers.dev.keycloak.entity.Users;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Users}.
 * Предоставляет базовые CRUD-операции и дополнительные методы поиска.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * Ищет пользователя по его {@code keycloakId}.
     *
     * @param keycloakId Идентификатор пользователя в Keycloak.
     * @return {@link Optional} содержащий {@link Users}, если найден.
     */
    Optional<Users> findByKeycloakId(String keycloakId);
}
