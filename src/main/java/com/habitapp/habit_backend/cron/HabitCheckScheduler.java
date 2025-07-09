package com.habitapp.habit_backend.cron;

import com.habitapp.habit_backend.model.*;
import com.habitapp.habit_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HabitCheckScheduler {

  private final HabitRepository habitRepository;
  private final HabitCheckRepository habitCheckRepository;
  private final ExerciseCheckRepository exerciseCheckRepository;

  public HabitCheckScheduler(
      HabitRepository habitRepository,
      HabitCheckRepository habitCheckRepository,
      ExerciseCheckRepository exerciseCheckRepository) {
    this.habitRepository = habitRepository;
    this.habitCheckRepository = habitCheckRepository;
    this.exerciseCheckRepository = exerciseCheckRepository;
  }

  @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
  @Transactional
  public void generateDailyHabitChecks() {
    List<Habit> allHabits = habitRepository.findAll();
    LocalDate today = LocalDate.now();

    for (Habit habit : allHabits) {
      // Vérifie si le HabitCheck du jour existe
      HabitCheck habitCheck = habitCheckRepository
          .findByHabitAndDateOfDay(habit, today)
          .orElseGet(() -> {
            HabitCheck newCheck = new HabitCheck(habit, today, false);
            return habitCheckRepository.save(newCheck);
          });

      // Pour chaque exercice, créer un ExerciseCheck s’il n’existe pas
      for (HabitExercise exercise : habit.getHabitExercises()) {
        boolean alreadyExists = exerciseCheckRepository
            .findByHabitCheckAndExercise(habitCheck, exercise)
            .isPresent();

        if (!alreadyExists) {
          ExerciseCheck exerciseCheck = ExerciseCheck.builder()
              .habitCheck(habitCheck)
              .exercise(exercise)
              .done(false)
              .build();
          exerciseCheckRepository.save(exerciseCheck);
        }
      }
    }
  }
}
