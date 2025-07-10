package com.habitapp.habit_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habitapp.habit_backend.service.LevelService;

@RestController
@RequestMapping("/level-system")
public class LevelSystemController {

  @Autowired
  private LevelService levelService;

  // Endpoint pour obtenir les informations de progression des niveaux
  @GetMapping("/progression")
  public ResponseEntity<?> getLevelProgression() {
    var progression = new LevelProgression();

    // Calculer la progression pour les 10 premiers niveaux
    for (int level = 1; level <= 10; level++) {
      int xpRequired = levelService.getXpRequiredForLevel(level);
      int totalXp = levelService.getTotalXpForLevel(level);

      progression.addLevel(new LevelDetail(level, xpRequired, totalXp));
    }

    return ResponseEntity.ok(progression);
  }

  // Endpoint pour simuler l'ajout d'XP et voir les changements de niveau
  @PostMapping("/simulate")
  public ResponseEntity<?> simulateXpGain(@RequestBody SimulateXpRequest request) {
    if (request.startingXp() < 0 || request.xpToAdd() <= 0) {
      return ResponseEntity.badRequest().body("Valeurs invalides");
    }

    int currentLevel = 1;
    int currentXp = request.startingXp();

    // Calculer le niveau de départ
    int tempXp = currentXp;
    while (tempXp >= levelService.getXpRequiredForLevel(currentLevel + 1)) {
      tempXp -= levelService.getXpRequiredForLevel(currentLevel + 1);
      currentLevel++;
    }

    int finalLevel = levelService.calculateMaxLevel(currentXp + request.xpToAdd(), currentLevel);

    // Calculer l'XP restant après montée de niveau
    int remainingXp = currentXp + request.xpToAdd();
    int tempLevel = currentLevel;
    while (remainingXp >= levelService.getXpRequiredForLevel(tempLevel + 1)) {
      remainingXp -= levelService.getXpRequiredForLevel(tempLevel + 1);
      tempLevel++;
    }

    return ResponseEntity.ok(new SimulationResult(
        currentLevel,
        currentXp,
        request.xpToAdd(),
        finalLevel,
        remainingXp,
        finalLevel - currentLevel));
  }

  // Classes internes pour les réponses
  public static class LevelProgression {
    private java.util.List<LevelDetail> levels = new java.util.ArrayList<>();

    public void addLevel(LevelDetail level) {
      levels.add(level);
    }

    public java.util.List<LevelDetail> getLevels() {
      return levels;
    }
  }

  public record LevelDetail(int level, int xpRequired, int totalXpFromStart) {
  }

  public record SimulateXpRequest(int startingXp, int xpToAdd) {
  }

  public record SimulationResult(
      int startingLevel,
      int startingXp,
      int xpAdded,
      int finalLevel,
      int remainingXp,
      int levelsGained) {
  }
}
