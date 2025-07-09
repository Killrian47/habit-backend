package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.repository.ExerciseCheckRepository;
import com.habitapp.habit_backend.repository.HabitCheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitCheckService {

  private final HabitCheckRepository habitCheckRepository;
  private final ExerciseCheckRepository exerciseCheckRepository;

  public HabitCheckService(
      HabitCheckRepository habitCheckRepository,
      ExerciseCheckRepository exerciseCheckRepository) {
    this.habitCheckRepository = habitCheckRepository;
    this.exerciseCheckRepository = exerciseCheckRepository;
  }

  @Transactional
  public void updateHabitCheckStatus(Habit habit) {
    HabitCheck check = habitCheckRepository
        .findByHabitAndDateOfDay(habit, LocalDate.now())
        .orElse(null);

    if (check == null)
      return;

    List<ExerciseCheck> checks = exerciseCheckRepository.findByHabitCheck(check);

    boolean allDone = checks.stream().allMatch(ExerciseCheck::isDone);

    check.setDone(allDone);
    habitCheckRepository.save(check);
  }
}
