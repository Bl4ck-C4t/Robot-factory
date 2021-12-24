package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class PartType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String name;

    public PartType(String name) {
        this.name = name;
    }

    public PartType() {
    }
}
