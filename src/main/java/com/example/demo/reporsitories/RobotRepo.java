package com.example.demo.reporsitories;

import com.example.demo.entities.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotRepo extends JpaRepository<Robot, Long> {
}
