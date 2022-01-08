package com.example.demo.reporsitories;

import com.example.demo.entities.Robot;
import com.example.demo.entities.RobotPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RobotRepo extends JpaRepository<Robot, Long> {
    Optional<Robot> findRobotByName(String name);
}
