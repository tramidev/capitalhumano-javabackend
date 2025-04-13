package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> findAll() {
        List<RoleDTO> roles = roleService.findAll();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findById(@PathVariable Long id) {
        RoleDTO roleDto = roleService.findById(id);
        if (roleDto.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roleDto);
        }
        return ResponseEntity.ok(roleDto);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO roleDto) {
        RoleDTO newRole = roleService.create(roleDto);
        if (newRole.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newRole);
        }
        return ResponseEntity.ok(newRole);
    }

    @PutMapping
    public ResponseEntity<RoleDTO> update(@RequestBody RoleDTO roleDto) {
        RoleDTO updated = roleService.update(roleDto);
        if (updated.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updated);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDTO> deleteById(@PathVariable Long id) {
        RoleDTO deleted = roleService.deleteById(id);
        if (deleted.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleted);
        }
        return ResponseEntity.ok(deleted);
    }
}
