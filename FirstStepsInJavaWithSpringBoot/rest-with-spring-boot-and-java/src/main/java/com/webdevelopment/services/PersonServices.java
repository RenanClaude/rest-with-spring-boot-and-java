package com.webdevelopment.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.model.entity.Person;
import com.webdevelopment.repositories.PersonRepository;

@Service
public class PersonServices {

	private PersonRepository repository;

	@Autowired
	public PersonServices(PersonRepository personRepository) {
		this.repository = personRepository;
	}

//	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public Person findById(Long id) {
		logger.info("Finding one person");
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}

	public List<Person> findAll() {
		logger.info("Finding all people");
		return repository.findAll();
	}

	public Person create(Person person) {
		logger.info("Creating one person");
		return repository.save(person);
	}

	public Person update(Person person) {
		logger.info("Updating one person");

		Person personToUpdate = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		personToUpdate.setAddress(person.getAddress());
		personToUpdate.setFirstName(person.getFirstName());
		personToUpdate.setGender(person.getGender());
		personToUpdate.setLastName(person.getLastName());

		return repository.save(personToUpdate);
	}

	public void delete(Long id) {
		logger.info("Deleting one person");

		Person personToDelete = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(personToDelete);
	}
}
