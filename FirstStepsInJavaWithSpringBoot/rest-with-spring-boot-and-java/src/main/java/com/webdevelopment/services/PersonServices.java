package com.webdevelopment.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.data.vo.v2.PersonVOV2;
import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.mapper.DozerMapper;
import com.webdevelopment.mapper.custom.PersonMapper;
import com.webdevelopment.model.entity.Person;
import com.webdevelopment.repositories.PersonRepository;

@Service
public class PersonServices {

	private PersonRepository repository;
	private PersonMapper mapper;

	@Autowired
	public PersonServices(PersonRepository personRepository, PersonMapper mapper) {
		this.repository = personRepository;
		this.mapper = mapper;
	}

//	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		return DozerMapper.parseObject(person, PersonVO.class);

	}

	public List<PersonVO> findAll() {
		logger.info("Finding all people");
		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO personVO) {
		logger.info("Creating one person");

		Person person = DozerMapper.parseObject(personVO, Person.class);
		Person personCreated = repository.save(person);
		return DozerMapper.parseObject(personCreated, PersonVO.class);
	}

	public PersonVOV2 createV2(PersonVOV2 personVOV2) {
		logger.info("Creating one person with V2");

		Person person = mapper.convertVoToEntity(personVOV2);
		Person personCreated = repository.save(person);
		return mapper.convertEntityToVo(personCreated);
	}

	public PersonVO update(PersonVO person) {
		logger.info("Updating one person");

		Person personToUpdate = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		personToUpdate.setAddress(person.getAddress());
		personToUpdate.setFirstName(person.getFirstName());
		personToUpdate.setGender(person.getGender());
		personToUpdate.setLastName(person.getLastName());

		Person personUpdated = repository.save(personToUpdate);

		return DozerMapper.parseObject(personUpdated, PersonVO.class);
	}

	public void delete(Long id) {
		logger.info("Deleting one person");

		Person personToDelete = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(personToDelete);
	}
}
