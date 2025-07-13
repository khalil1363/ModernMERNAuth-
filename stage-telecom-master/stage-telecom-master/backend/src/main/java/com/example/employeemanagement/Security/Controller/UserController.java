package com.example.employeemanagement.Security.Controller;

import com.example.employeemanagement.Security.Model.Role;
import com.example.employeemanagement.Security.Model.User;
import com.example.employeemanagement.Security.Model.UserUpdateRequest;
import com.example.employeemanagement.Security.Repository.UserRepository;
import com.example.employeemanagement.Security.Service.UserService;
import com.example.employeemanagement.Security.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.createUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during registration: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<User>>  getallusers(){
    List<User> users = userService.getallusers();
    return ResponseEntity.ok(users);
      }

    @PostMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
            return jwtUtil.VTK(token);
    }
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {

            return ResponseEntity.ok(userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")));

    }
    @DeleteMapping("/{id}")
    public void SupprimerUser(@PathVariable Long id){
        this.userService.supprimer(id);


    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest updateRequest) {

        User updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
