package com.example.demo.reporsitories;

import com.example.demo.entities.City;
import com.example.demo.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepo extends JpaRepository<Location, Long> {
    @Query("select l from Location l left join City c on (c.cityName = :cityName and c = l.city) where l.address = :address")
    public Optional<Location> getLocationByCityAndAddress(String cityName, String address);
}
