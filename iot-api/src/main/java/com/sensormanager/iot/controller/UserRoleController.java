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

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.service.UserRoleService;

@Controller
@RequestMapping("/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> findAll() {
        List<UserRoleDTO> userRoles = userRoleService.findAll();
        if (userRoles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> findByUserId(@PathVariable Integer id) {
        UserRoleDTO userRoleDto = userRoleService.findByUserId(id);
        if (userRoleDto.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(userRoleDto);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userRoleDto);
    }

    @PostMapping
    public ResponseEntity<UserRoleDTO> create(@RequestBody UserRoleDTO userRoleDto) {
        UserRoleDTO newUserRole = userRoleService.create(userRoleDto);
        if (newUserRole.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(newUserRole);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(newUserRole);
    }

    @PutMapping
    public ResponseEntity<UserRoleDTO> update(@RequestBody UserRoleDTO userRoleDTO) {
        UserRoleDTO userRoleUpdate = userRoleService.update(userRoleDTO);
        if (userRoleUpdate.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(userRoleUpdate);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(userRoleUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserRoleDTO> deleteByUserId(@PathVariable Integer id) {
        UserRoleDTO deleteUserRole = userRoleService.deleteById(id);
        if (deleteUserRole.getUserId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(deleteUserRole);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(deleteUserRole);
    }

}
