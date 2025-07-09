package com.habitapp.habit_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "habit_exercises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitExercise {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private int xpGiven;

  @ManyToOne
  @JoinColumn(name = "habit_id", nullable = false)
  @JsonBackReference
  private Habit habit;

}
