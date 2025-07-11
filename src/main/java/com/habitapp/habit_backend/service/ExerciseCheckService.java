package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.model.HabitExercise;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.ExerciseCheckRepository;
import com.habitapp.habit_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseCheckService {

  private final ExerciseCheckRepository exerciseCheckRepository;
  private final HabitCheckService habitCheckService;
  private final UserRepository userRepository;
  private final LevelingService levelingService;

  @Transactional
  public void markAsDone(Long id) {
    ExerciseCheck check = exerciseCheckRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("ExerciseCheck not found"));

    if (check.isDone())
      return;

    check.setDone(true);
    exerciseCheckRepository.save(check);

    // Met à jour HabitCheck si tous les exos sont faits
    habitCheckService.updateHabitCheckStatus(check.getHabitCheck().getHabit());

    // 🔥 Ajout d’XP au user
    HabitExercise habitExercise = check.getExercise();
    User user = habitExercise.getHabit().getUser();

    int earnedXp = habitExercise.getXpGiven();
    user.setXp(user.getXp() + earnedXp);

    // Met à jour le niveau si nécessaire
    levelingService.applyLeveling(user);

    userRepository.save(user);
  }
}
