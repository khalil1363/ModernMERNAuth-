package com.example.employeemanagement.Security.Model;


public class AuthRequest {

    private String username;
    private String password;

    // Constructeur par défaut (nécessaire pour la désérialisation JSON)
    public AuthRequest() {
    }

    // Constructeur avec paramètres
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters et Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}