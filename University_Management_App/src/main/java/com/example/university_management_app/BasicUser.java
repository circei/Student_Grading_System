package com.example.university_management_app;
public class BasicUser implements User {
    private String username;
    private String role;

    public BasicUser(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getRole() {
        return role;
    }
}
