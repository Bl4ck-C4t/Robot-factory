package com.example.demo.entities;

import javax.persistence.*;
import java.util.Set;
import java.util.function.BinaryOperator;

@Entity
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public String name;
    public Double cost;

    @ManyToOne()
    @JoinColumn(name = "factory_id")
    public Factory origin;

    @ManyToMany
    @JoinTable(
            name = "robot_parts",
            joinColumns = @JoinColumn(name = "robot_id"),
            inverseJoinColumns = @JoinColumn(name = "robot_part_id")
    )
    public Set<RobotPart> parts;

    public Robot(String name, Factory origin) {
        this.name = name;
        this.cost = 0d;
        this.origin = origin;
    }

    public Robot() {
    }

    public void updateCost() {
        cost = 0d;
        parts.forEach(p -> cost += p.value);
        cost += parts.size() * 0.5;
    }
}
