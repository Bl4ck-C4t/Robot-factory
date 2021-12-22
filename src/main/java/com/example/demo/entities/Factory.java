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

}
