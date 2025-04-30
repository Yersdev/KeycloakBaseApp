package yers.dev.keycloak.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yers.dev.keycloak.entity.dto.AuthRequest;
import yers.dev.keycloak.entity.Users;
import yers.dev.keycloak.entity.dto.UsersDto;
import yers.dev.keycloak.mapper.UsersMapper;
import yers.dev.keycloak.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями в локальной базе данных.
 * Обеспечивает операции получения, регистрации и обновления данных пользователей.
 */
@Service
@AllArgsConstructor
public class UsersService {

    /**
     * Репозиторий для работы с сущностями {@link Users}.
     */
    private final UsersRepository usersRepository;

    /**
     * Получает список всех пользователей из базы данных.
     *
     * @return список пользователей в виде {@link UsersDto}
     */
    public List<UsersDto> getAll() {
        List<Users> users = usersRepository.findAll();
        List<UsersDto> dtos = new ArrayList<>(users.size());
        for (Users user : users) {
            dtos.add(UsersMapper.toUsersDto(user));
        }
        return dtos;
    }

    /**
     * Получает информацию о текущем пользователе по его Keycloak ID.
     *
     * @param keycloakId идентификатор пользователя в Keycloak
     * @return данные пользователя в виде {@link UsersDto}
     * @throws ResponseStatusException если пользователь не найден
     */
    public UsersDto getMe(String keycloakId) {
        Users user = usersRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с id " + keycloakId + " не найден"
                ));
        return UsersMapper.toUsersDto(user);
    }

    /**
     * Регистрирует нового пользователя в локальной базе данных.
     *
     * @param req        DTO с данными для регистрации
     * @param keycloakId ID пользователя в Keycloak
     */
    @Transactional
    public void registerUser(AuthRequest req, String keycloakId) {
        Users u = new Users();
        u.setKeycloakId(keycloakId);
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        usersRepository.save(u);
    }

    /**
     * Обновляет данные пользователя в базе по его Keycloak ID.
     *
     * @param req        DTO с обновлёнными данными
     * @param keycloakId ID пользователя в Keycloak
     * @throws ResponseStatusException если пользователь не найден
     */
    @Transactional
    public void updateUser(AuthRequest req , String keycloakId) {
        Users user = usersRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + keycloakId));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        usersRepository.save(user);
    }
}