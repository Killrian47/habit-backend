package com.habitapp.habit_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitapp.habit_backend.model.User;

@Service
public class RewardService {

  @Autowired
  private LevelService levelService;

  // XP accordé pour différentes actions
  public static final int XP_COMPLETE_HABIT = 20;
  public static final int XP_COMPLETE_EXERCISE = 15;
  public static final int XP_DAILY_STREAK = 10;
  public static final int XP_WEEKLY_STREAK = 50;

  // Récompenser un utilisateur pour avoir terminé une habitude
  public User rewardHabitCompletion(User user) {
    return levelService.addXp(user, XP_COMPLETE_HABIT);
  }

  // Récompenser un utilisateur pour avoir terminé un exercice
  public User rewardExerciseCompletion(User user) {
    return levelService.addXp(user, XP_COMPLETE_EXERCISE);
  }

  // Récompenser un utilisateur pour une série quotidienne
  public User rewardDailyStreak(User user) {
    return levelService.addXp(user, XP_DAILY_STREAK);
  }

  // Récompenser un utilisateur pour une série hebdomadaire
  public User rewardWeeklyStreak(User user) {
    return levelService.addXp(user, XP_WEEKLY_STREAK);
  }

  // Récompenser avec un montant d'XP personnalisé
  public User rewardCustomXp(User user, int xp) {
    return levelService.addXp(user, xp);
  }
}
