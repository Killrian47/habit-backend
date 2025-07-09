package com.habitapp.habit_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.habitapp.habit_backend.repository.UserRepository;
import com.habitapp.habit_backend.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    User appUser = userRepository.findByMail(mail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(appUser.getMail())
        .password(appUser.getPassword())
        .roles("USER")
        .build();
  }

}
