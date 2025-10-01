package com.example.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "employee")
public class Employee {
    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Set<String> roles = new HashSet<>();

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_CUSTOMER_SERVICE = "CUSTOMER_SERVICE";
    public static final String ROLE_FINANCE_EXECUTION = "FINANCE_EXECUTION";
    public static final String ROLE_MARKETING_EXECUTIVE = "MARKETING_EXECUTIVE";

    public Employee() {}

    public Employee(String name, String email, String password, Set<String> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        setRoles(roles);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) {
        this.roles.clear();
        if (roles != null) {
            for (String role : roles) {
                if (isValidRole(role)) this.roles.add(role);
            }
        }
    }

    private boolean isValidRole(String role) {
        return role != null && (
                role.equals(ROLE_ADMIN) ||
                        role.equals(ROLE_CUSTOMER_SERVICE) ||
                        role.equals(ROLE_FINANCE_EXECUTION) ||
                        role.equals(ROLE_MARKETING_EXECUTIVE)
        );
    }
}