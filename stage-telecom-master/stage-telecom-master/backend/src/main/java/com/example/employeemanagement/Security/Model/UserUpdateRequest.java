package com.example.employeemanagement.Security.Model;

    import lombok.Data;

    @Data
    public class UserUpdateRequest {
        private String username;
        private String email;
        private String password;
    }

