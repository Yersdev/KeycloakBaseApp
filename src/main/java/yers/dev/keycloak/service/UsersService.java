package yers.dev.keycloak.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yers.dev.keycloak.entity.Users;
import yers.dev.keycloak.entity.dto.UsersDto;
import yers.dev.keycloak.mapper.UsersMapper;
import yers.dev.keycloak.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public List<UsersDto> getAll() {
        List<Users> users = usersRepository.findAll();
        List<UsersDto> dtos = new ArrayList<>(users.size());
        for (Users user : users) {
            dtos.add(UsersMapper.toUsersDto(user));
        }
        return dtos;
    }


    public UsersDto getMe(String jwt) {
        return null;
    }
}
