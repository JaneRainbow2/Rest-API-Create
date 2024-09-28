package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userRequestDto) {
        log.info("[POST] Request to create user");
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRole(roleService.readById(2));
        userService.create(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(new UserResponseDto(user));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #id")
    public UserResponseDto read(@PathVariable long id) {
        log.info("[GET] Request to read user");
        return new UserResponseDto(userService.readById(id));
    }

    @PatchMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.principal.id == #id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDto> update(@PathVariable long id,
                                    @RequestBody UserRequestDto userRequestDto) {
        log.info("[Patch] Request to update user");
        User oldUser = userService.readById(id);
        oldUser.setFirstName(userRequestDto.getFirstName());
        oldUser.setLastName(userRequestDto.getLastName());
        oldUser.setEmail(userRequestDto.getEmail());
        oldUser.setPassword(userRequestDto.getPassword());
        userService.update(oldUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(oldUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(new UserResponseDto(oldUser));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("[DELETE] Request to delete user");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDto> getAll() {
        log.info("[GET] Request to read all users");
        return userService.getAll().stream()
                .map(UserResponseDto:: new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/todos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #id")
    public List<ToDoResponseDto> getAllToDo(@PathVariable long id) {
        log.info("[GET] Request to read ToDo in user");
        return userService.readById(id)
                .getMyTodos()
                .stream()
                .map(ToDoResponseDto:: new)
                .collect(Collectors.toList());
    }
}