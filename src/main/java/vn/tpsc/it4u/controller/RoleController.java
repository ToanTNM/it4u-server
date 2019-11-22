package vn.tpsc.it4u.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.repository.RoleRepository;

/**
 * RoleRepository
 */
@RestController
@RequestMapping("${app.api.version}/role")
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @GetMapping()
    public ResponseEntity<?> get() {
        List<Role> roles = repository.findAll();

        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Role role) {
        repository.save(role);

        return ResponseEntity.ok("Inserted");
    }
}