package com.habitapp.habit_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "exercise_checks")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ExerciseCheck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Builder.Default
  private boolean done = false;

  @ManyToOne(optional = false)
  private HabitExercise exercise;

  @ManyToOne(optional = false)
  private HabitCheck habitCheck;
}
