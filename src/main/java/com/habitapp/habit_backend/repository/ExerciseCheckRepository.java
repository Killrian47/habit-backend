package com.habitapp.habit_backend.repository;

import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.model.HabitExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseCheckRepository extends JpaRepository<ExerciseCheck, Long> {

  List<ExerciseCheck> findByHabitCheck(HabitCheck habitCheck);

  Optional<ExerciseCheck> findByHabitCheckAndExercise(HabitCheck habitCheck, HabitExercise exercise);
}
