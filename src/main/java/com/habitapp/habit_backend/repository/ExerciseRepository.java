package com.habitapp.habit_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habitapp.habit_backend.model.Exercise;
import com.habitapp.habit_backend.model.User;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
  List<Exercise> findByUser(User user);
}