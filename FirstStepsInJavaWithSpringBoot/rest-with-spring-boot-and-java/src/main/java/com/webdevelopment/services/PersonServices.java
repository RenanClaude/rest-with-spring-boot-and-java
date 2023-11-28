package com.webdevelopment.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.webdevelopment.controllers.PersonController;
import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.exceptions.RequiredObjectIsNullException;
import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.mapper.DozerMapper;
import com.webdevelopment.model.entity.Person;
import com.webdevelopment.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {

	private PersonRepository repository;
	private PagedResourcesAssembler<PersonVO> assembler;

	@Autowired
	public PersonServices(
			PersonRepository personRepository, PagedResourcesAssembler<PersonVO> assembler
			) {
		this.repository = personRepository;
		this.assembler = assembler;
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

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
		logger.info("Finding all people");
		
		Page<Person> personPage = repository.findAll(pageable);
		
		Page<PersonVO> personVOsPage = personPage.map(person -> DozerMapper.parseObject(person, PersonVO.class));
		
		personVOsPage.stream().forEach(
				person -> person.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel()));
		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return this.assembler.toModel(personVOsPage, link);
	}

	public PersonVO create(PersonVO personVO) {
		
		if (personVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one person");
		
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
	
	@Transactional
	public PersonVO disablePerson(Long id) {
		logger.info("Disabling one person");
		
		repository.disablePerson(id);
		
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		PersonVO personVO = DozerMapper.parseObject(person, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	}

	public void delete(Long id) {
		logger.info("Deleting one person");

		Person personToDelete = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(personToDelete);
	}
}
