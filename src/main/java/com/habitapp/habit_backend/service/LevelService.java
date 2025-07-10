package com.habitapp.habit_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;

@Service
public class LevelService {

  @Autowired
  private UserRepository userRepository;

  // Formule pour calculer l'XP requis pour un niveau donné
  // Niveau 1: 100 XP, Niveau 2: 150 XP, Niveau 3: 200 XP, etc.
  public int getXpRequiredForLevel(int level) {
    if (level <= 1) {
      return 100;
    }
    return 100 + (level - 1) * 50; // Progression: 100, 150, 200, 250, 300...
  }

  // Calculer l'XP total nécessaire pour atteindre un niveau
  public int getTotalXpForLevel(int targetLevel) {
    int totalXp = 0;
    for (int level = 1; level < targetLevel; level++) {
      totalXp += getXpRequiredForLevel(level);
    }
    return totalXp;
  }

  // Calculer combien d'XP il reste pour le niveau suivant
  public int getXpToNextLevel(User user) {
    int xpRequiredForNextLevel = getXpRequiredForLevel(user.getLevel() + 1);
    return xpRequiredForNextLevel - user.getXp();
  }

  // Calculer le niveau maximum atteignable avec l'XP actuel
  public int calculateMaxLevel(int currentXp, int currentLevel) {
    int tempXp = currentXp;
    int level = currentLevel;

    while (tempXp >= getXpRequiredForLevel(level + 1)) {
      tempXp -= getXpRequiredForLevel(level + 1);
      level++;
    }

    return level;
  }

  // Ajouter de l'XP à un utilisateur et mettre à jour son niveau
  public User addXp(User user, int xpToAdd) {
    user.setXp(user.getXp() + xpToAdd);

    // Vérifier si l'utilisateur peut monter de niveau
    while (user.getXp() >= getXpRequiredForLevel(user.getLevel() + 1)) {
      int xpRequiredForNextLevel = getXpRequiredForLevel(user.getLevel() + 1);
      user.setXp(user.getXp() - xpRequiredForNextLevel);
      user.setLevel(user.getLevel() + 1);
    }

    return userRepository.save(user);
  }

  // Obtenir les informations détaillées du niveau
  public LevelInfo getLevelInfo(User user) {
    int xpForCurrentLevel = user.getXp();
    int xpRequiredForNextLevel = getXpRequiredForLevel(user.getLevel() + 1);
    int xpToNextLevel = xpRequiredForNextLevel - xpForCurrentLevel;
    double progressPercentage = (double) xpForCurrentLevel / xpRequiredForNextLevel * 100;

    return new LevelInfo(
        user.getLevel(),
        xpForCurrentLevel,
        xpRequiredForNextLevel,
        xpToNextLevel,
        progressPercentage);
  }

  // Record pour les informations de niveau
  public record LevelInfo(
      int currentLevel,
      int currentXp,
      int xpRequiredForNextLevel,
      int xpToNextLevel,
      double progressPercentage) {
  }
}
