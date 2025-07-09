package com.habitapp.habit_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "habit_checks")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HabitCheck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean isDone = false;

  private LocalDate dateOfDay;

  @ManyToOne
  @JoinColumn(name = "habit_id", nullable = false)
  @JsonBackReference
  private Habit habit;

  public HabitCheck(Habit habit, LocalDate date, boolean completed) {
    this.habit = habit;
    this.dateOfDay = date;
    this.isDone = completed;
  }

  public HabitCheck(Habit habit) {
    this.habit = habit;
    this.dateOfDay = LocalDate.now();
    this.isDone = false;
  }

}
