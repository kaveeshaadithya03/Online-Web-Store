package com.example.test.repository;

import com.example.test.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByEmail(String email);
    Employee findByEmail(String email);
    List<Employee> findByRolesContaining(String role);
}