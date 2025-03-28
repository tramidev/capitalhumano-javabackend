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

import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.service.RoleService;

@Controller
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(roleDto);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(roleDto);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO roleDto) {
        RoleDTO newRole = roleService.create(roleDto);
        if (newRole.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(newRole);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(newRole);
    }

    @PutMapping
    public ResponseEntity<RoleDTO> update(@RequestBody RoleDTO roleDTO) {
        RoleDTO roleUpdate = roleService.update(roleDTO);
        if (roleUpdate.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(roleUpdate);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(roleUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleDTO> deleteById(@PathVariable Long id) {
        RoleDTO deleteRole = roleService.deleteById(id);
        if (deleteRole.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(deleteRole);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(deleteRole);
    }

}
