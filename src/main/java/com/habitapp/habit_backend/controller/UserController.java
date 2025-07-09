package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser() {
    String mail = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepository.findByMail(mail);

    if (userOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    User user = userOptional.get();

    // Ne retourne pas le mot de passe dans la réponse !
    return ResponseEntity.ok(new UserInfoResponse(
        user.getId(),
        user.getUsername(),
        user.getMail(),
        user.getXp(),
        user.getLevel()));
  }

  // DTO interne pour masquer le mot de passe
  public record UserInfoResponse(
      Long id,
      String username,
      String mail,
      int xp,
      int level) {
  }
}
