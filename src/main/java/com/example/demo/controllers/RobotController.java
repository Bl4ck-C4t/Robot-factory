package com.example.demo.controllers;

import com.example.demo.entities.Robot;
import com.example.demo.entities.RobotPart;
import com.example.demo.reporsitories.PartTypeRepo;
import com.example.demo.reporsitories.RobotPartRepo;
import com.example.demo.reporsitories.RobotRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/robot")
public class RobotController {

    private RobotRepo robotRepo;

    public RobotController(RobotRepo robotRepo) {
        this.robotRepo = robotRepo;
    }

    @GetMapping(value = "/fetch")
    @ResponseStatus(HttpStatus.OK)
    public List<Robot> getAllRobots(){
        return robotRepo.findAll();
    }


}
