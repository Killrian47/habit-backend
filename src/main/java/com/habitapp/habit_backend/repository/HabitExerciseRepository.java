package com.habitapp.habit_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.HabitExercise;

import java.util.List;

public interface HabitExerciseRepository extends JpaRepository<HabitExercise, Long> {
  List<HabitExercise> findByHabit(Habit habit);
}
