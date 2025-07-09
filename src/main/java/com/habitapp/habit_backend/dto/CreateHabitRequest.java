package com.habitapp.habit_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateHabitRequest {
  private String title;
  private String color;
  private boolean archived;
  private List<ExerciseRequest> exercises;

  @Data
  public static class ExerciseRequest {
    private String name;
    private String description;
    private int xpGiven;

    // Getters & Setters
  }

  // Getters & Setters
}
