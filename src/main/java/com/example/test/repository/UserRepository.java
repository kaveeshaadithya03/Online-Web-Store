package com.example.test.repository;

import com.example.test.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    @Query("{ 'role' : ?0 }")
    List<User> findByRole(String role);
}