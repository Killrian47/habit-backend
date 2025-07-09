package com.habitapp.habit_backend.controller;

import com.habitapp.habit_backend.service.ExerciseCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercise-checks")
public class ExerciseCheckController {

  private final ExerciseCheckService exerciseCheckService;

  public ExerciseCheckController(ExerciseCheckService exerciseCheckService) {
    this.exerciseCheckService = exerciseCheckService;
  }

  @PostMapping("/{id}/done")
  public ResponseEntity<?> markExerciseAsDone(@PathVariable Long id) {
    exerciseCheckService.markAsDone(id);
    return ResponseEntity.ok().build();
  }
}
