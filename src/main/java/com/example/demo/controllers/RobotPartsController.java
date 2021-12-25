package com.example.demo.controllers;

import com.example.demo.entities.PartType;
import com.example.demo.entities.Robot;
import com.example.demo.entities.RobotPart;
import com.example.demo.reporsitories.PartTypeRepo;
import com.example.demo.reporsitories.RobotPartRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/robot/parts")
public class RobotPartsController {
    private PartTypeRepo partTypeRepo;
    private RobotPartRepo robotPartRepo;

    public RobotPartsController(PartTypeRepo partTypeRepo, RobotPartRepo robotPartRepo) {
        this.partTypeRepo = partTypeRepo;
        this.robotPartRepo = robotPartRepo;
    }

    @GetMapping(value = "/fetchTypes")
    @ResponseStatus(HttpStatus.OK)
    public List<PartType> getAllRobotPartTypes(){
        return partTypeRepo.findAll();
    }

    @DeleteMapping(value = "/deleteType")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRobotPartType(@RequestParam String name){
        partTypeRepo.deletePartTypeByName(name);
        return "Deleted!";
    }

    @PostMapping(value = "/makeType")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeRobotPartType(@RequestParam String name){
        partTypeRepo.save(new PartType(name));
        return "Part type created!";
    }

    @GetMapping(value = "/fetchParts")
    @ResponseStatus(HttpStatus.OK)
    public List<RobotPart> getAllRobotParts(){
        return robotPartRepo.findAll();
    }

    @DeleteMapping(value = "/deletePart")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRobotPart(@RequestParam String name){
        robotPartRepo.deleteRobotPartByName(name);
        return "Deleted!";
    }

    @PostMapping(value = "/buildPart")
    @ResponseStatus(HttpStatus.CREATED)
    public String constructRobotPart(HttpServletResponse response,
                                                @RequestParam String partType, @RequestParam String name,
                                                @RequestParam String desc, @RequestParam Double weight){
        Optional<PartType> type = partTypeRepo.findPartTypeByName(partType);
        if(type.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No such part type!";
        }

        robotPartRepo.save(new RobotPart(type.get(), name, desc, weight));

        return "Part created!";
    }
}
