package yers.dev.keycloak.mapper;

import yers.dev.keycloak.entity.Users;
import yers.dev.keycloak.entity.dto.UsersDto;

public class UsersMapper {

    public static Users toUsers(UsersDto usersDto) {
        Users users = new Users();
        users.setUsername(usersDto.getUsername());
        users.setEmail(usersDto.getEmail());
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        return users;
    }

    public static UsersDto toUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setUsername(users.getUsername());
        usersDto.setEmail(users.getEmail());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        return usersDto;
    }
}
