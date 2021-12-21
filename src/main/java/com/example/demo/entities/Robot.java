package com.example.demo.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String name;
    public Double cost;

    @ManyToMany
    @JoinTable(
            name = "robot_parts",
            joinColumns = @JoinColumn(name = "robot_id"),
            inverseJoinColumns = @JoinColumn(name = "robot_part_id")
    )
    public Set<RobotPart> parts;

    public Robot() {
    }
}
