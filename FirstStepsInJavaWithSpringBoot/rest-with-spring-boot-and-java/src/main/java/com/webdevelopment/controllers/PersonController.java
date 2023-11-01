package com.webdevelopment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webdevelopment.model.entity.Person;
import com.webdevelopment.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

//	private final AtomicLong counter = new AtomicLong();

//	private MathService mathService;
	private PersonServices personServices;

	@Autowired
	public PersonController(PersonServices personServices) {
//		this.mathService = mathService;
		this.personServices = personServices;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> findById(@PathVariable(value = "id") Long id) {
		Person person = this.personServices.findById(id);

		return ResponseEntity.status(HttpStatus.OK).body(person);

	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Person>> findAll() {
		List<Person> persons = this.personServices.findAll();

		return ResponseEntity.status(HttpStatus.OK).body(persons);

	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> create(@RequestBody Person person) {
		Person createdPerson = this.personServices.create(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);

	}

	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> update(@RequestBody Person person) {
		Person updatedPerson = this.personServices.update(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(updatedPerson);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		this.personServices.delete(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

	}
//
//	@GetMapping("/subtraction/{firstNumber}/{secondNumber}")
//	public ResponseEntity<Double> subtraction(@PathVariable(value = "firstNumber") String firstNumber,
//			@PathVariable(value = "secondNumber") String secondNumber) throws Exception {
//
//		Double result = this.mathService.subtractionService(firstNumber, secondNumber);
//
//		return ResponseEntity.status(HttpStatus.OK).body(result);
//	}
//
//	@GetMapping("/multiplication/{firstNumber}/{secondNumber}")
//	public ResponseEntity<Double> multiplication(@PathVariable(value = "firstNumber") String firstNumber,
//			@PathVariable(value = "secondNumber") String secondNumber) throws Exception {
//
//		Double result = this.mathService.multiplicationService(firstNumber, secondNumber);
//
//		return ResponseEntity.status(HttpStatus.OK).body(result);
//	}
//
//	@GetMapping("/division/{firstNumber}/{secondNumber}")
//	public ResponseEntity<Double> division(@PathVariable(value = "firstNumber") String firstNumber,
//			@PathVariable(value = "secondNumber") String secondNumber) throws Exception {
//
//		Double result = this.mathService.divisionService(firstNumber, secondNumber);
//
//		return ResponseEntity.status(HttpStatus.OK).body(result);
//	}
//
//	@GetMapping("/average/{firstNumber}/{secondNumber}")
//	public ResponseEntity<Double> average(@PathVariable(value = "firstNumber") String firstNumber,
//			@PathVariable(value = "secondNumber") String secondNumber) throws Exception {
//
//		Double result = this.mathService.averageService(firstNumber, secondNumber);
//
//		return ResponseEntity.status(HttpStatus.OK).body(result);
//	}
//
//	@GetMapping("/squareroot/{firstNumber}")
//	public ResponseEntity<Double> squareRoot(@PathVariable(value = "firstNumber") String firstNumber) throws Exception {
//
//		Double result = this.mathService.squareRootService(firstNumber);
//
//		return ResponseEntity.status(HttpStatus.OK).body(result);
//	}

}
