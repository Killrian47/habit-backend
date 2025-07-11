package com.habitapp.habit_backend.service;

import com.habitapp.habit_backend.model.User;
import com.habitapp.habit_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User getByEmail(String email) {
    return userRepository.findByMail(email)
        .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
  }
}
