package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.repository.ExerciseCheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ExerciseCheckService {

  private final ExerciseCheckRepository exerciseCheckRepository;
  private final HabitCheckService habitCheckService;

  public ExerciseCheckService(
      ExerciseCheckRepository exerciseCheckRepository,
      HabitCheckService habitCheckService) {
    this.exerciseCheckRepository = exerciseCheckRepository;
    this.habitCheckService = habitCheckService;
  }

  @Transactional
  public void markAsDone(Long id) {
    ExerciseCheck check = exerciseCheckRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("ExerciseCheck not found"));

    check.setDone(true);
    exerciseCheckRepository.save(check);

    // Vérifie si tous les exos sont faits et met à jour le HabitCheck
    habitCheckService.updateHabitCheckStatus(check.getHabitCheck().getHabit());
  }
}
