package com.habitapp.habit_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseCheckDTO {
  private Long id;
  private boolean done;
  private Long exerciseId;
  private String exerciseName;
  private String exerciseDescription;
  private int xpGiven;
  private Long habitId;
  private String habitTitle;
  private String habitColor;
  private Long habitCheckId;
}
