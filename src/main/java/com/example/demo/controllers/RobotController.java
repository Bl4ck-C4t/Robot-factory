package com.example.demo.controllers;

import com.example.demo.entities.Factory;
import com.example.demo.entities.Robot;
import com.example.demo.entities.RobotPart;
import com.example.demo.reporsitories.FactoryRepo;
import com.example.demo.reporsitories.RobotPartRepo;
import com.example.demo.reporsitories.RobotRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/robot")
public class RobotController {

    private final RobotRepo robotRepo;
    private final FactoryRepo factoryRepo;
    private final RobotPartRepo robotPartRepo;

    public RobotController(RobotRepo robotRepo, FactoryRepo factoryRepo, RobotPartRepo robotPartRepo) {
        this.robotRepo = robotRepo;
        this.factoryRepo = factoryRepo;
        this.robotPartRepo = robotPartRepo;
    }

    @GetMapping(value = "/fetch")
    @ResponseStatus(HttpStatus.OK)
    public List<Robot> getAllRobots(){
        return robotRepo.findAll();
    }

    @PostMapping(value = "/buildRobot")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeRobot(HttpServletResponse response,
                           @RequestParam String name,
                           @RequestParam String factoryName ){
        Optional<Factory> factOpt = factoryRepo.findFactoryByName(factoryName);
        if(factOpt.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No such factory!";
        }

        robotRepo.save(new Robot(name, factOpt.get()));
        return "Robot constructed.";
    }

    @PutMapping(value = "{id}/addPart")
    @ResponseStatus(HttpStatus.CREATED)
    public String addRobotPart(HttpServletResponse response,
                               @PathVariable Long id, @RequestParam String partName){
        Optional<Robot> roboOpt = robotRepo.findById(id);
        if(roboOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot with that id.";
        }

        Optional<RobotPart> roboPartOpt = robotPartRepo.findRobotPartByName(partName);
        if(roboPartOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot part with that name.";
        }

        Robot robot = roboOpt.get();
        robot.parts.add(roboPartOpt.get());
        robot.updateCost();
        robotRepo.save(robot);

        return "Part added";
    }



}
