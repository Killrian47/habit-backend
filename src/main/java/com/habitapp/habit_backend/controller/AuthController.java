package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.auth.AuthRequest;
import com.habitapp.habit_backend.auth.AuthResponse;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import com.habitapp.habit_backend.service.JwtService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AuthRequest request) {
    if (userRepository.findByMail(request.getMail()).isPresent()) {
      return ResponseEntity.badRequest().body("Email already used");
    }

    User user = new User();
    user.setMail(request.getMail());
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setXp(0);
    user.setLevel(1);

    userRepository.save(user);

    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
    if (userOpt.isEmpty()) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    User user = userOpt.get();
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
