package com.example.test.repository;

import com.example.test.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByEmail(String email);
    Employee findByEmail(String email);
}