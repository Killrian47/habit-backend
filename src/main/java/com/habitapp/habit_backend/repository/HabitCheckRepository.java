package com.habitapp.habit_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.HabitCheck;

import java.time.LocalDate;
import java.util.Optional;

public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {
  Optional<HabitCheck> findByHabitAndDateOfDay(Habit habit, LocalDate dateOfDay);

  boolean existsByHabitAndDateOfDay(Habit habit, LocalDate dateOfDay);
}