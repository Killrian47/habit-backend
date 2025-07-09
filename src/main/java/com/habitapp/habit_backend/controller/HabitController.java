package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.dto.CreateHabitRequest;
import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import com.habitapp.habit_backend.service.HabitService;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

  @Autowired
  private HabitService habitService;

  @Autowired
  private UserRepository userRepository;

  private User getCurrentUser() {
    String mail = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository.findByMail(mail).orElseThrow();
  }

  @GetMapping
  public List<Habit> getMyHabits() {
    return habitService.getHabitsForUser(getCurrentUser());
  }

  @PostMapping
  public ResponseEntity<Habit> createHabit(@RequestBody CreateHabitRequest request) {
    Habit created = habitService.createHabitForUser(getCurrentUser(), request);
    return ResponseEntity.ok(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteHabit(@PathVariable Long id) {
    boolean deleted = habitService.deleteHabit(id, getCurrentUser());
    if (deleted)
      return ResponseEntity.noContent().build();
    return ResponseEntity.status(403).body("Not allowed");
  }
}
