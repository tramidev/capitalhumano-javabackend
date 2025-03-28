package com.sensormanager.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO userDto = userService.findById(id);
        if (userDto.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(userDto);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDto) {
        UserDTO newUser = userService.create(userDto);
        if (newUser.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteById(@PathVariable Long id) {
        UserDTO deleteUser = userService.deleteById(id);
        if (deleteUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(deleteUser);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(deleteUser);
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO) {
        UserDTO userUpdate = userService.update(userDTO);
        if (userUpdate.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(userUpdate);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userUpdate);
    }

}
