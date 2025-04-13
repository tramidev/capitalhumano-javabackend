package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    // Solo requiere userId, ya que solo hay un rol por usuario
    @GetMapping("/{userId}")
    public ResponseEntity<UserRoleDTO> findByUserId(@PathVariable Integer userId) {
        UserRoleDTO dto = userRoleService.findByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserRoleDTO> create(@RequestBody UserRoleDTO userRoleDto) {
        UserRoleDTO newUserRole = userRoleService.create(userRoleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserRole);
    }

    @PutMapping
    public ResponseEntity<UserRoleDTO> update(@RequestBody UserRoleDTO userRoleDto) {
        UserRoleDTO updated = userRoleService.update(userRoleDto);
        return ResponseEntity.ok(updated);
    }

    // Eliminamos por userId (porque se asume un solo rol por usuario)
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserRoleDTO> deleteByUserId(@PathVariable Integer userId) {
        UserRoleDTO deleted = userRoleService.deleteById(userId);
        return ResponseEntity.ok(deleted);
    }
}
