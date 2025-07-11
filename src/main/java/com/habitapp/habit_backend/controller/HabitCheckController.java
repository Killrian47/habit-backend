package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.HabitCheckRepository;
import com.habitapp.habit_backend.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habit-checks")
public class HabitCheckController {

  @Autowired
  private HabitCheckRepository habitCheckRepository;

  @Autowired
  private UserService userService;

  // Récupérer toutes les checks d'un utilisateur
  @GetMapping
  public ResponseEntity<List<HabitCheck>> getAllHabitChecks(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    List<HabitCheck> habitChecks = habitCheckRepository.findByHabitUser(user);
    return ResponseEntity.ok(habitChecks);
  }

  // Récupérer les checks d'un utilisateur pour une date spécifique
  @GetMapping("/date/{date}")
  public ResponseEntity<List<HabitCheck>> getHabitChecksByDate(
      @PathVariable LocalDate date,
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    List<HabitCheck> habitChecks = habitCheckRepository.findByDateOfDayAndHabitUser(date, user);
    return ResponseEntity.ok(habitChecks);
  }

  // Récupérer les checks d'aujourd'hui pour l'utilisateur
  @GetMapping("/today")
  public ResponseEntity<List<HabitCheck>> getTodayHabitChecks(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    LocalDate today = LocalDate.now();
    List<HabitCheck> habitChecks = habitCheckRepository.findByDateOfDayAndHabitUser(today, user);
    return ResponseEntity.ok(habitChecks);
  }

  // Récupérer une check spécifique par ID
  @GetMapping("/{id}")
  public ResponseEntity<HabitCheck> getHabitCheckById(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    Optional<HabitCheck> habitCheck = habitCheckRepository.findById(id);

    if (habitCheck.isPresent()) {
      User user = userService.getByEmail(userDetails.getUsername());
      // Vérifier que la check appartient à l'utilisateur connecté
      if (habitCheck.get().getHabit().getUser().getId().equals(user.getId())) {
        return ResponseEntity.ok(habitCheck.get());
      }
    }
    return ResponseEntity.notFound().build();
  }

  // Marquer une habit check comme terminée
  @PostMapping("/{id}/complete")
  public ResponseEntity<?> markHabitCheckAsComplete(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    Optional<HabitCheck> habitCheckOpt = habitCheckRepository.findById(id);

    if (habitCheckOpt.isPresent()) {
      HabitCheck habitCheck = habitCheckOpt.get();
      User user = userService.getByEmail(userDetails.getUsername());

      // Vérifier que la check appartient à l'utilisateur connecté
      if (habitCheck.getHabit().getUser().getId().equals(user.getId())) {
        habitCheck.setDone(true);
        habitCheckRepository.save(habitCheck);
        return ResponseEntity.ok(new HabitCheckResponse(
            habitCheck.getId(),
            habitCheck.isDone(),
            habitCheck.getDateOfDay(),
            habitCheck.getHabit().getId(),
            habitCheck.getHabit().getTitle()));
      }
    }
    return ResponseEntity.notFound().build();
  }

  // Marquer une habit check comme non terminée
  @PostMapping("/{id}/uncomplete")
  public ResponseEntity<?> markHabitCheckAsUncomplete(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    Optional<HabitCheck> habitCheckOpt = habitCheckRepository.findById(id);

    if (habitCheckOpt.isPresent()) {
      HabitCheck habitCheck = habitCheckOpt.get();
      User user = userService.getByEmail(userDetails.getUsername());

      // Vérifier que la check appartient à l'utilisateur connecté
      if (habitCheck.getHabit().getUser().getId().equals(user.getId())) {
        habitCheck.setDone(false);
        habitCheckRepository.save(habitCheck);
        return ResponseEntity.ok(new HabitCheckResponse(
            habitCheck.getId(),
            habitCheck.isDone(),
            habitCheck.getDateOfDay(),
            habitCheck.getHabit().getId(),
            habitCheck.getHabit().getTitle()));
      }
    }
    return ResponseEntity.notFound().build();
  }

  // Record pour la réponse
  public record HabitCheckResponse(
      Long id,
      boolean isDone,
      LocalDate dateOfDay,
      Long habitId,
      String habitTitle) {
  }
}
