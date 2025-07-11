package com.habitapp.habit_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.HabitCheck;
import com.habitapp.habit_backend.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitCheckRepository extends JpaRepository<HabitCheck, Long> {
  Optional<HabitCheck> findByHabitAndDateOfDay(Habit habit, LocalDate dateOfDay);

  boolean existsByHabitAndDateOfDay(Habit habit, LocalDate dateOfDay);

  // Récupérer toutes les checks d'un utilisateur
  @Query("SELECT hc FROM HabitCheck hc WHERE hc.habit.user = :user")
  List<HabitCheck> findByHabitUser(@Param("user") User user);

  // Récupérer les checks d'un utilisateur pour une date spécifique
  @Query("SELECT hc FROM HabitCheck hc WHERE hc.dateOfDay = :date AND hc.habit.user = :user")
  List<HabitCheck> findByDateOfDayAndHabitUser(@Param("date") LocalDate date, @Param("user") User user);

  // Récupérer les checks d'une habitude spécifique
  List<HabitCheck> findByHabit(Habit habit);

  // Récupérer les checks d'une habitude pour une période donnée
  @Query("SELECT hc FROM HabitCheck hc WHERE hc.habit = :habit AND hc.dateOfDay BETWEEN :startDate AND :endDate")
  List<HabitCheck> findByHabitAndDateOfDayBetween(@Param("habit") Habit habit, @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}