package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import com.habitapp.habit_backend.service.LevelService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LevelService levelService;

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

  @PutMapping("/update")
  public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request) {
    String mail = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepository.findByMail(mail);

    if (userOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    User existingUser = userOptional.get();

    // Validation des données
    if (request.username() == null || request.username().trim().isEmpty()) {
      return ResponseEntity.badRequest().body("Username ne peut pas être vide");
    }

    if (request.username().length() < 3 || request.username().length() > 50) {
      return ResponseEntity.badRequest().body("Username doit contenir entre 3 et 50 caractères");
    }

    // Vérifier si le username existe déjà (si différent de l'actuel)
    if (!request.username().equals(existingUser.getUsername())) {
      Optional<User> existingUsername = userRepository.findByUsername(request.username());
      if (existingUsername.isPresent()) {
        return ResponseEntity.badRequest().body("Ce nom d'utilisateur est déjà utilisé");
      }
    }

    try {
      // Mettre à jour seulement le username (pas l'email pour des raisons de
      // sécurité)
      existingUser.setUsername(request.username().trim());

      User savedUser = userRepository.save(existingUser);

      return ResponseEntity.ok(new UserInfoResponse(
          savedUser.getId(),
          savedUser.getUsername(),
          savedUser.getMail(),
          savedUser.getXp(),
          savedUser.getLevel()));

    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Erreur lors de la mise à jour");
    }
  }

  // Endpoint pour obtenir les informations détaillées du niveau
  @GetMapping("/level-info")
  public ResponseEntity<?> getLevelInfo() {
    String mail = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepository.findByMail(mail);

    if (userOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    User user = userOptional.get();
    LevelService.LevelInfo levelInfo = levelService.getLevelInfo(user);

    return ResponseEntity.ok(levelInfo);
  }

  // Endpoint pour ajouter de l'XP (utile pour tester ou pour des récompenses)
  @PostMapping("/add-xp")
  public ResponseEntity<?> addXp(@RequestBody AddXpRequest request) {
    String mail = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepository.findByMail(mail);

    if (userOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (request.xp() <= 0) {
      return ResponseEntity.badRequest().body("L'XP à ajouter doit être positif");
    }

    User user = userOptional.get();
    int previousLevel = user.getLevel();

    // Ajouter l'XP et mettre à jour le niveau
    User updatedUser = levelService.addXp(user, request.xp());

    boolean leveledUp = updatedUser.getLevel() > previousLevel;

    return ResponseEntity.ok(new AddXpResponse(
        updatedUser.getLevel(),
        updatedUser.getXp(),
        request.xp(),
        leveledUp,
        leveledUp ? "Félicitations ! Vous avez atteint le niveau " + updatedUser.getLevel() + " !"
            : "XP ajouté avec succès"));
  }

  // Record pour les données de mise à jour (sans email)
  public record UserUpdateRequest(String username) {
  }

  public record AddXpRequest(int xp) {
  }

  public record AddXpResponse(
      int newLevel,
      int remainingXp,
      int xpAdded,
      boolean leveledUp,
      String message) {
  }

  public record UserInfoResponse(
      Long id,
      String username,
      String mail,
      int xp,
      int level) {
  }
}
