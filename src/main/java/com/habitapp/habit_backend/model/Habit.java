package com.habitapp.habit_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "habits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Habit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String color;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HabitExercise> habitExercises;

  @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonBackReference
  private List<HabitCheck> habitChecks;

}
