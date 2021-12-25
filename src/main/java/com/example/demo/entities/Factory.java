package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class Factory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String name;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    public Location location;

    public Factory(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Factory() {
    }
}
