package yers.dev.keycloak.mapper;

import yers.dev.keycloak.entity.Users;
import yers.dev.keycloak.entity.dto.UsersDto;

/**
 * Класс {@code UsersMapper} содержит методы для преобразования между сущностью {@link Users}
 * и DTO-объектом {@link UsersDto}.
 */
public class UsersMapper {

    /**
     * Преобразует объект {@link UsersDto} в объект {@link Users}.
     *
     * @param usersDto DTO, содержащий данные пользователя.
     * @return Сущность {@link Users}, построенная на основе данных DTO.
     */
    public static Users toUsers(UsersDto usersDto) {
        Users users = new Users();
        users.setEmail(usersDto.getEmail());
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        return users;
    }

    /**
     * Преобразует объект {@link Users} в объект {@link UsersDto}.
     *
     * @param users Сущность пользователя из базы данных.
     * @return DTO {@link UsersDto}, содержащий только нужные поля.
     */
    public static UsersDto toUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setEmail(users.getEmail());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        return usersDto;
    }
}
