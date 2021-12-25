package com.example.demo.reporsitories;

import com.example.demo.entities.PartType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartTypeRepo extends JpaRepository<PartType, Long> {
    public Optional<PartType> findPartTypeByName(String name);
    public void deletePartTypeByName(String name);
}
