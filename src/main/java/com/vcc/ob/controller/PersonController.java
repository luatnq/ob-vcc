package com.vcc.ob.controller;

import com.vcc.ob.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;

    @GetMapping("/update")
    public ResponseEntity<?> updatePerson(@RequestParam(name = "person_name") String personName,
                                          @RequestParam(name = "person_id") int personId) throws SQLException {
        personService.updatePerson(personName, personId);

        return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> selectPerson(@RequestParam(name = "person_id") int personId) throws SQLException {
        return new ResponseEntity<>(personService.selectPerson(personId), HttpStatus.OK);
    }
}
