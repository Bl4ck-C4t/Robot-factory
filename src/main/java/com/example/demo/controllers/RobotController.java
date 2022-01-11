package com.example.demo.controllers;

import com.example.demo.entities.Factory;
import com.example.demo.entities.Order;
import com.example.demo.entities.Robot;
import com.example.demo.entities.RobotPart;
import com.example.demo.reporsitories.FactoryRepo;
import com.example.demo.reporsitories.RobotPartRepo;
import com.example.demo.reporsitories.RobotRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/robot")
@PreAuthorize("hasAnyAuthority('ROLE_ENGINEER', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ENGINEER', 'ROLE_ADMIN')")
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
    public String addRobotPart(HttpServletResponse response,
                               @PathVariable Long id, @RequestParam String partName){
        Optional<Robot> robotOpt = robotRepo.findById(id);
        if(robotOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot with that id.";
        }

        Optional<RobotPart> roboPartOpt = robotPartRepo.findRobotPartByName(partName);
        if(roboPartOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot part with that name.";
        }

        Robot robot = robotOpt.get();
        robot.parts.add(roboPartOpt.get());
        robot.updateCost();
        robotRepo.save(robot);
        response.setStatus(HttpStatus.CREATED.value());
        if(roboPartOpt.get().name.contains("tea")){
            response.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
        }
        return "Part added";
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRobot(HttpServletResponse response, @RequestParam String robotName){
        Optional<Robot> robotOpt = robotRepo.findRobotByName(robotName);
        if (robotOpt.isEmpty()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "No such robot!";
        }
        robotRepo.delete(robotOpt.get());
        return "Robot deleted!";
    }

    @PutMapping(value = "/{part_id}/changeValue")
    @ResponseStatus(HttpStatus.OK)
    public String changePartValye(HttpServletResponse response, @PathVariable Long part_id, Double value){
        Optional<RobotPart> roboPartOpt = robotPartRepo.findById(part_id);
        if(roboPartOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot part with that name.";
        }
        roboPartOpt.get().value = value;
        robotPartRepo.save(roboPartOpt.get());
        return "Price updated";
    }

}
