package com.webdevelopment.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webdevelopment.controllers.PersonController;
import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.exceptions.RequiredObjectIsNullException;
import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.mapper.DozerMapper;
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

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		PersonVO personVO = DozerMapper.parseObject(person, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	}

	public List<PersonVO> findAll() {
		logger.info("Finding all people");
		List<PersonVO> peopleVO = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);

		peopleVO.stream().forEach(
				person -> person.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel()));

		return peopleVO;
	}

	public PersonVO create(PersonVO personVO) {
		
		if (personVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one person");
		
		System.out.println("AQUI: " + personVO);

		Person person = DozerMapper.parseObject(personVO, Person.class);
		Person personCreated = repository.save(person);
		PersonVO resPersonVO = DozerMapper.parseObject(personCreated, PersonVO.class);
		resPersonVO.add(linkTo(methodOn(PersonController.class).findById(resPersonVO.getKey())).withSelfRel());
		return resPersonVO;
	}

	public PersonVO update(PersonVO person) {
		
		if (person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one person");

		Person personToUpdate = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		personToUpdate.setAddress(person.getAddress());
		personToUpdate.setFirstName(person.getFirstName());
		personToUpdate.setGender(person.getGender());
		personToUpdate.setLastName(person.getLastName());

		Person personUpdated = repository.save(personToUpdate);

		PersonVO personVO = DozerMapper.parseObject(personUpdated, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		return personVO;
	}

	public void delete(Long id) {
		logger.info("Deleting one person");

		Person personToDelete = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(personToDelete);
	}
}
