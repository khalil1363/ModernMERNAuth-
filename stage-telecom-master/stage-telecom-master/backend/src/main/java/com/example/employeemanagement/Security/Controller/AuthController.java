package  com.example.employeemanagement.Security.Controller;

import com.example.employeemanagement.Security.Model.AuthRequest;
import com.example.employeemanagement.Security.config.CustomUserDetailsService;
import com.example.employeemanagement.Security.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", userDetails.getUsername());
            response.put("role", userDetails.getAuthorities());

            return ResponseEntity.ok(response); // ✅ retourne un objet JSON
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during authentication");
        }
    }


}