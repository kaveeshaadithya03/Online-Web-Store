package com.example.test.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority().replace("ROLE_", "");
        String redirectUrl = getRedirectUrl(role);
        response.sendRedirect(redirectUrl);
    }

    private String getRedirectUrl(String role) {
        return switch (role) {
            case "admin" -> "/AdminDashboard.html";
            case "customer" -> "/customer.html";
            case "seller" -> "/sellerProperties.html";
            case "CUSTOMER_SERVICE" -> "/customer_service.html";
            case "FINANCE_EXECUTION" -> "/finance_execution.html";
            case "MARKETING_EXECUTIVE" -> "/marketing_executive.html";
            default -> "/login.html?error=invalid_role";
        };
    }
}