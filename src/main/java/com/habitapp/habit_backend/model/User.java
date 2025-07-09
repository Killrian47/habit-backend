package com.habitapp.habit_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @Column(nullable = false, unique = true)
  private String mail;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  private int xp;
  private int level;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Habit> habits;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Exercise> exercises;

}
