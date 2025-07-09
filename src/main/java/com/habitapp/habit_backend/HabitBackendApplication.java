package com.habitapp.habit_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HabitBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitBackendApplication.class, args);
	}

}
