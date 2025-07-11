package com.habitapp.habit_backend.repository;

import com.habitapp.habit_backend.model.ExerciseCheck;
import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.model.HabitExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExerciseCheckRepository extends JpaRepository<ExerciseCheck, Long> {

  List<ExerciseCheck> findByHabitCheck(HabitCheck habitCheck);

  Optional<ExerciseCheck> findByHabitCheckAndExercise(HabitCheck habitCheck, HabitExercise exercise);

  @Query("SELECT ec FROM ExerciseCheck ec WHERE ec.habitCheck.habit.id = :habitId")
  List<ExerciseCheck> findByHabitId(@Param("habitId") Long habitId);
}
