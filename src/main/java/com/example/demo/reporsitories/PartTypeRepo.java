package com.example.demo.reporsitories;

import com.example.demo.entities.PartType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartTypeRepo extends JpaRepository<PartType, Long> {
}
