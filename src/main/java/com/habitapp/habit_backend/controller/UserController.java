package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.dto.LevelProgressDTO;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import com.habitapp.habit_backend.service.LevelingService;
import com.habitapp.habit_backend.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LevelingService levelingService;

  @Autowired
  private UserService userService;

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

  // Endpoint pour récupérer le niveau et la progression de l'utilisateur
  @GetMapping("/level")
  public ResponseEntity<LevelProgressDTO> getUserLevel(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    LevelProgressDTO progress = levelingService.getUserLevelProgress(user);
    return ResponseEntity.ok(progress);
  }

  // Mettre à jour le niveau de l'utilisateur
  @GetMapping("/level/apply-xp")
  public ResponseEntity<?> applyLeveling(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    levelingService.applyLeveling(user);
    LevelProgressDTO progress = levelingService.getUserLevelProgress(user);
    return ResponseEntity.ok(progress);
  }

  public record UserInfoResponse(
      Long id,
      String username,
      String mail,
      int xp,
      int level) {
  }
}
