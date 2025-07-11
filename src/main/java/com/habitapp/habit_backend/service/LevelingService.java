package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.dto.LevelProgressDTO;
import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LevelingService {

  private final UserRepository userRepository;

  // Méthode à appeler quand un utilisateur gagne de l'XP
  public void applyLeveling(User user) {
    int currentXp = user.getXp();
    int level = user.getLevel();

    // Calcul de l'XP requis pour atteindre le niveau suivant
    int xpToNextLevel = getXpRequiredForLevel(level);

    // Tant qu'on a assez d'XP pour monter de niveau
    while (currentXp >= xpToNextLevel) {
      currentXp -= xpToNextLevel;
      level++;
      xpToNextLevel = getXpRequiredForLevel(level);
    }

    user.setXp(currentXp);
    user.setLevel(level);
    userRepository.save(user);
  }

  public LevelProgressDTO getUserLevelProgress(User user) {
    int level = user.getLevel();
    int currentXp = user.getXp();
    int requiredXp = getXpRequiredForLevel(level);

    return new LevelProgressDTO(level, currentXp, requiredXp);
  }

  // Méthode pour calculer l'XP requis selon le niveau
  private int getXpRequiredForLevel(int level) {
    return 100 + (level - 1) * 50;
  }
}
