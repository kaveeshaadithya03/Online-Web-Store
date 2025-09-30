package com.example.test.controller;

import com.example.test.model.Employee;
import com.example.test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/admins")
    public List<Employee> getAdmins() {
        return employeeRepository.findByRolesContaining(Employee.ROLE_ADMIN);
    }

    @GetMapping("/non-admins")
    public List<Employee> getNonAdmins() {
        List<String> nonAdminRoles = Arrays.asList(
                Employee.ROLE_CUSTOMER_SERVICE,
                Employee.ROLE_FINANCE_EXECUTION,
                Employee.ROLE_MARKETING_EXECUTIVE
        );
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getRoles().stream().anyMatch(nonAdminRoles::contains))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee updatedEmployee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (!existingEmployee.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Employee existingEmployeeWithEmail = employeeRepository.findByEmail(updatedEmployee.getEmail());
        if (existingEmployeeWithEmail != null && !existingEmployeeWithEmail.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        updatedEmployee.setId(id);
        if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
            updatedEmployee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        } else {
            updatedEmployee.setPassword(existingEmployee.get().getPassword());
        }
        Employee savedEmployee = employeeRepository.save(updatedEmployee);
        return ResponseEntity.ok(savedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}