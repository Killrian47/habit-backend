package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.dto.ExerciseCheckDTO;
import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.model.HabitExercise;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.ExerciseCheckRepository;
import com.habitapp.habit_backend.repository.HabitCheckRepository;
import com.habitapp.habit_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseCheckService {

  private final ExerciseCheckRepository exerciseCheckRepository;
  private final HabitCheckService habitCheckService;
  private final UserRepository userRepository;
  private final LevelingService levelingService;
  private final HabitCheckRepository habitCheckRepository;

  public List<ExerciseCheck> findByHabitId(Long habitId) {
    return exerciseCheckRepository.findByHabitId(habitId);
  }

  public List<ExerciseCheck> getTodayExerciseChecksForUser(User user) {
    LocalDate today = LocalDate.now();

    // Récupérer tous les HabitCheck du jour pour l'utilisateur
    List<HabitCheck> todayHabitChecks = habitCheckRepository.findByDateOfDayAndHabitUser(today, user);

    // Récupérer tous les ExerciseCheck liés à ces HabitCheck
    List<ExerciseCheck> allExerciseChecks = new ArrayList<>();
    for (HabitCheck habitCheck : todayHabitChecks) {
      List<ExerciseCheck> exerciseChecks = exerciseCheckRepository.findByHabitCheck(habitCheck);
      allExerciseChecks.addAll(exerciseChecks);
    }

    return allExerciseChecks;
  }

  public List<ExerciseCheckDTO> getTodayExerciseChecksDTOForUser(User user) {
    LocalDate today = LocalDate.now();

    // Récupérer tous les HabitCheck du jour pour l'utilisateur
    List<HabitCheck> todayHabitChecks = habitCheckRepository.findByDateOfDayAndHabitUser(today, user);

    // Récupérer tous les ExerciseCheck liés à ces HabitCheck et les convertir en
    // DTO
    List<ExerciseCheckDTO> allExerciseCheckDTOs = new ArrayList<>();
    for (HabitCheck habitCheck : todayHabitChecks) {
      List<ExerciseCheck> exerciseChecks = exerciseCheckRepository.findByHabitCheck(habitCheck);

      for (ExerciseCheck exerciseCheck : exerciseChecks) {
        ExerciseCheckDTO dto = new ExerciseCheckDTO();
        dto.setId(exerciseCheck.getId());
        dto.setDone(exerciseCheck.isDone());
        dto.setExerciseId(exerciseCheck.getExercise().getId());
        dto.setExerciseName(exerciseCheck.getExercise().getName());
        dto.setExerciseDescription(exerciseCheck.getExercise().getDescription());
        dto.setXpGiven(exerciseCheck.getExercise().getXpGiven());
        dto.setHabitId(exerciseCheck.getHabitCheck().getHabit().getId());
        dto.setHabitTitle(exerciseCheck.getHabitCheck().getHabit().getTitle());
        dto.setHabitColor(exerciseCheck.getHabitCheck().getHabit().getColor());
        dto.setHabitCheckId(exerciseCheck.getHabitCheck().getId());

        allExerciseCheckDTOs.add(dto);
      }
    }

    return allExerciseCheckDTOs;
  }

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
