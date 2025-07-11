package com.habitapp.habit_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelProgressDTO {
  private int level;
  private int currentXp;
  private int requiredXp;

}
