package com.habitapp.habit_backend.controller;

import com.habitapp.habit_backend.dto.ExerciseCheckDTO;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.service.ExerciseCheckService;
import com.habitapp.habit_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise-checks")
public class ExerciseCheckController {

  private final ExerciseCheckService exerciseCheckService;
  private final UserService userService;

  public ExerciseCheckController(ExerciseCheckService exerciseCheckService, UserService userService) {
    this.exerciseCheckService = exerciseCheckService;
    this.userService = userService;
  }

  @GetMapping("/today")
  public ResponseEntity<List<ExerciseCheckDTO>> getTodayExerciseChecks(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.getByEmail(userDetails.getUsername());
    List<ExerciseCheckDTO> todayExerciseChecks = exerciseCheckService.getTodayExerciseChecksDTOForUser(user);
    return ResponseEntity.ok(todayExerciseChecks);
  }

  @PostMapping("/{id}/done")
  public ResponseEntity<?> markExerciseAsDone(@PathVariable Long id) {
    exerciseCheckService.markAsDone(id);
    return ResponseEntity.ok().build();
  }
}
