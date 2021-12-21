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

    public RobotPart() {
    }
}
