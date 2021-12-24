package com.example.demo.reporsitories;

import com.example.demo.entities.RobotPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RobotPartRepo extends JpaRepository<RobotPart, Long> {

}
