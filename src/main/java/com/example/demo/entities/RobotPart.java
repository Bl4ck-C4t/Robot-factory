package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class RobotPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "part_type_id")
    public PartType type;

    public String name;

    public String description;

    public Double weight;

    public Double value;

    public RobotPart(PartType type, String name, String description, Double weight) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = 0d;
    }

    public RobotPart() {
    }
}
