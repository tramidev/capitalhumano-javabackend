package com.sensormanager.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.service.UserService;

@RestController
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
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user ID: " + id + " does not exist.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDto) {
        UserDTO newUser = userService.create(userDto);
        if (newUser.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user was not inserted.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteById(@PathVariable Long id) {
        UserDTO deletedUser = userService.deleteById(id);
        if (deletedUser.getId() == null || Boolean.TRUE.equals(deletedUser.getUserStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user was not disabled.");
        }
        return ResponseEntity.ok(deletedUser);
    }


    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO) {
        UserDTO userUpdate = userService.update(userDTO);
        if (userUpdate.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user was not updated.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userUpdate);
    }

}
