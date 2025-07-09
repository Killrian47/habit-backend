package com.habitapp.habit_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitapp.habit_backend.dto.CreateHabitRequest;
import com.habitapp.habit_backend.model.Habit;
import com.habitapp.habit_backend.model.HabitExercise;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.HabitRepository;

@Service
public class HabitService {

  @Autowired
  private HabitRepository habitRepository;

  /*
   * @Autowired
   * private UserRepository userRepository;
   */

  public List<Habit> getHabitsForUser(User user) {
    return habitRepository.findByUser(user);
  }

  public Habit createHabitForUser(User user, CreateHabitRequest dto) {
    Habit habit = new Habit();
    habit.setUser(user);
    habit.setTitle(dto.getTitle());
    habit.setColor(dto.getColor());

    List<HabitExercise> exercises = new ArrayList<>();
    if (dto.getExercises() != null) {
      for (CreateHabitRequest.ExerciseRequest e : dto.getExercises()) {
        HabitExercise ex = new HabitExercise();
        ex.setName(e.getName());
        ex.setDescription(e.getDescription());
        ex.setXpGiven(e.getXpGiven());
        ex.setHabit(habit);
        exercises.add(ex);
      }
    }

    habit.setHabitExercises(exercises);

    return habitRepository.save(habit);
  }

  public boolean deleteHabit(Long habitId, User user) {
    Optional<Habit> habitOpt = habitRepository.findById(habitId);
    if (habitOpt.isPresent() && habitOpt.get().getUser().getId().equals(user.getId())) {
      habitRepository.deleteById(habitId);
      return true;
    }
    return false;
  }
}
