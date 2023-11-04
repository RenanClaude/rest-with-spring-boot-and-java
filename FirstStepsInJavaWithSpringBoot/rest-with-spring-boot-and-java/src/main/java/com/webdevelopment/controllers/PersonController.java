package com.webdevelopment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.data.vo.v2.PersonVOV2;
import com.webdevelopment.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

//	private final AtomicLong counter = new AtomicLong();

	private PersonServices personServices;

	@Autowired
	public PersonController(PersonServices personServices) {
		this.personServices = personServices;
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVO> findById(@PathVariable(value = "id") Long id) {
		PersonVO person = this.personServices.findById(id);

		return ResponseEntity.status(HttpStatus.OK).body(person);

	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonVO>> findAll() {
		List<PersonVO> persons = this.personServices.findAll();

		return ResponseEntity.status(HttpStatus.OK).body(persons);

	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
		PersonVO createdPerson = this.personServices.create(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
	}

	@PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVOV2> createV2(@RequestBody PersonVOV2 person) {
		PersonVOV2 createdPerson = this.personServices.createV2(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonVO> update(@RequestBody PersonVO person) {
		PersonVO updatedPerson = this.personServices.update(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedPerson);

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		this.personServices.delete(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
