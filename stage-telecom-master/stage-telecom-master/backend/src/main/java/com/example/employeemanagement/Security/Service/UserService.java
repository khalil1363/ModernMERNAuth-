package com.example.employeemanagement.Security.Service;

import com.example.employeemanagement.Security.Model.Role;
import com.example.employeemanagement.Security.Model.User;
import com.example.employeemanagement.Security.Model.UserUpdateRequest;
import com.example.employeemanagement.Security.Repository.UserRepository;
import com.example.employeemanagement.Security.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateRequest updateRequest) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        User user = optionalUser.get();

        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getPassword() != null) {

            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        return userRepository.save(user);
    }


    public List<User> getallusers(){
        return  this.userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void supprimer(Long id){
        this.userRepository.delete(this.userRepository.getById(id));
    }
}