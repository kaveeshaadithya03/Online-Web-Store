package com.example.test.controller;

import com.example.test.model.Admin;
import com.example.test.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@Validated
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepository.save(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable String id, @Valid @RequestBody Admin updatedAdmin) {
        Optional<Admin> existingAdmin = adminRepository.findById(id);
        if (!existingAdmin.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Admin existingAdminWithEmail = adminRepository.findByEmail(updatedAdmin.getEmail());
        if (existingAdminWithEmail != null && !existingAdminWithEmail.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        updatedAdmin.setId(id);
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            updatedAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        } else {
            updatedAdmin.setPassword(existingAdmin.get().getPassword());
        }
        Admin savedAdmin = adminRepository.save(updatedAdmin);
        return ResponseEntity.ok(savedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}