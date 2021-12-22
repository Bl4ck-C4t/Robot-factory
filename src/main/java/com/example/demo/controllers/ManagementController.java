package com.example.demo.controllers;

import com.example.demo.entities.City;
import com.example.demo.entities.Location;
import com.example.demo.reporsitories.CityRepo;
import com.example.demo.reporsitories.LocationRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/manage")
public class ManagementController {
    private final LocationRepo locationRepo;
    private final CityRepo cityRepo;

    public ManagementController(LocationRepo locationRepo, CityRepo cityRepo) {
        this.locationRepo = locationRepo;
        this.cityRepo = cityRepo;
    }

    @PostMapping(value = "/makeLocation")
    @ResponseStatus(HttpStatus.CREATED)
    public String createLocation(@RequestParam String address, @RequestParam String cityName){
        Optional<City> cityOpt = cityRepo.findCityByCityName(cityName);
        City city = cityOpt.orElseGet(() -> cityRepo.save(new City(cityName)));
        locationRepo.save(new Location(city, address));
        return "Location Created!";
    }

    @GetMapping(value = "/fetchLocations")
    @ResponseStatus(HttpStatus.OK)
    public List<Location> fetchLocations(){
        return locationRepo.findAll();
    }

    @PostMapping(value = "/makeFactory")
    @ResponseStatus(HttpStatus.CREATED)
    public String createFactory(String name, Location location){
        return "";
    }
}
