package com.example.demo.controllers;

import com.example.demo.entities.City;
import com.example.demo.entities.Client;
import com.example.demo.reporsitories.CityRepo;
import com.example.demo.reporsitories.ClientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/city")
@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
public class CityController {
    private final CityRepo cityRepo;

    public CityController(CityRepo repo){
        this.cityRepo = repo;
    }

    @GetMapping(value = "/fetch")
    public List<City> getAllCities(){
        return cityRepo.findAll();
    }

    @GetMapping(value = "/getCity")
    public ResponseEntity<?> getCity(String name){
        Optional<City> city = cityRepo.findCityByCityName(name);
        if(city.isPresent()){
            return ResponseEntity.ok(city);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/makeCity")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCity(String name){
        cityRepo.save(new City(name));
        return "City Created!";
    }

    @DeleteMapping(value = "/deleteCity")
    public String deleteCity(HttpServletResponse response, String name){
        Optional<City> ToDelete = cityRepo.findCityByCityName(name);
        if(ToDelete.isEmpty()){
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "City not found :(";
        }
        cityRepo.delete(ToDelete.get());
        return "City Deleted!";
    }

}
