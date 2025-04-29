package yers.dev.keycloak.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yers.dev.keycloak.entity.dto.UsersDto;
import yers.dev.keycloak.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UsersService usersService;

    @GetMapping("/all")
    public ResponseEntity<List<UsersDto>> getAll(){
        return ResponseEntity
                .status(200)
                .body(usersService.getAll());
    }

    @GetMapping("/me")
    public ResponseEntity<UsersDto> getMe(String jwt){
        return ResponseEntity
                .status(200)
                .body(usersService.getMe(jwt));
    }
}
