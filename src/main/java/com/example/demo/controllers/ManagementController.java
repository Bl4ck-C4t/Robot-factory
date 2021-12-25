package com.example.demo.controllers;

import com.example.demo.entities.City;
import com.example.demo.entities.Factory;
import com.example.demo.entities.Location;
import com.example.demo.reporsitories.CityRepo;
import com.example.demo.reporsitories.FactoryRepo;
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
    private final FactoryRepo factoryRepo;

    public ManagementController(LocationRepo locationRepo, CityRepo cityRepo, FactoryRepo factoryRepo) {
        this.locationRepo = locationRepo;
        this.cityRepo = cityRepo;
        this.factoryRepo = factoryRepo;
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
    public String createFactory(@RequestParam String name, @RequestParam String address, @RequestParam String cityName){
        Optional<Location> locOpt = locationRepo.getLocationByCityAndAddress(cityName, address);
        if(locOpt.isEmpty())
            createLocation(address, cityName);
        Location loc = locationRepo.getLocationByCityAndAddress(cityName, address).get();
        factoryRepo.save(new Factory(name, loc));
        return "Factory created!";
    }

    @GetMapping(value = "/fetchFactories")
    @ResponseStatus(HttpStatus.OK)
    public List<Factory> fetchFactories(){
        return factoryRepo.findAll();
    }

    @DeleteMapping(value = "/deleteFactory")
    @ResponseStatus(HttpStatus.OK)
    public String deleteFactory(@RequestParam String name){
        factoryRepo.deleteFactoryByName(name);
        return "Deleted";
    }
}
