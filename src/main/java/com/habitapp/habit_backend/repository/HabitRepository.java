package com.habitapp.habit_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.User;

public interface HabitRepository extends JpaRepository<Habit, Long> {
  List<Habit> findByUser(User user);

}
