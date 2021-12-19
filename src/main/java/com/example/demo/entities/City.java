package com.example.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "city")
public class City {

    public City(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public City(String cityName) {
        this.cityName = cityName;
    }

    @Column(name = "cityName")
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }
}
