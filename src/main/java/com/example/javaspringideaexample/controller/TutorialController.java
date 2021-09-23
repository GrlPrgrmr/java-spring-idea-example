package com.example.javaspringideaexample.controller;


import com.example.javaspringideaexample.model.Tutorial;
import com.example.javaspringideaexample.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins ="http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialRepository tutRepo;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title){
        try {
            List<Tutorial> tuts = new ArrayList<>();

            if(title == null){
                tutRepo.findAll().forEach(tuts::add);
            }
            else
            {
                tutRepo.findByTitleContaining(title).forEach(tuts::add);
            }

            if(tuts.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tuts,HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id")long id){
        Optional<Tutorial> tutData = tutRepo.findById(id);

        if(tutData.isPresent()){
            return new ResponseEntity<>(tutData.get(),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial)
    {
        try
        {
            Tutorial _tutorial = tutRepo.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(),false));
            return new ResponseEntity<>(_tutorial,HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id")long id, @RequestBody Tutorial tutorial){
        Optional<Tutorial> tutData = tutRepo.findById(id);

        if(tutData.isPresent()){
            Tutorial _tut = tutData.get();
            _tut.setTitle(tutorial.getTitle());
            _tut.setDescription(tutorial.getDescription());
            _tut.setPublished(tutorial.isPublished());

            return new ResponseEntity<>(tutRepo.save(_tut),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished(){
        try{
            List<Tutorial> tuts = tutRepo.findByPublished(true);

            if(tuts.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tuts,HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
