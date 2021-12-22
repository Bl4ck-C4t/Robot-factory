package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    public City city;

    public String address;

    public Location(City city, String address) {
        this.city = city;
        this.address = address;
    }

    public Location() {
    }
}
