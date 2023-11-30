package com.webdevelopment.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.exceptions.RequiredObjectIsNullException;
import com.webdevelopment.model.entity.Person;
import com.webdevelopment.repositories.PersonRepository;
import com.webdevelopment.services.PersonServices;
import com.webdevelopment.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;

	@InjectMocks
	PersonServices service;
	@Mock
	PersonRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		PersonVO result = service.findById(1L);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}



	@Test
	void testCreate() {
		// Entity before repository
		Person person = input.mockEntity(1);

		// Entity after repository
		Person personPersisted = person;
		personPersisted.setId(1L);

		PersonVO personVO = input.mockVO(1);
		personVO.setKey(1L);

		when(repository.save(person)).thenReturn(personPersisted);

		PersonVO result = service.create(personVO);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testCreateWithNullPerson() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not to allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		Person person = input.mockEntity(1);
		person.setId(null);

		Person personPersisted = person;
		personPersisted.setId(1L);

		PersonVO personVO = input.mockVO(1);
		personVO.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));
		when(repository.save(person)).thenReturn(personPersisted);

		PersonVO result = service.update(personVO);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testUpdateWithNullPerson() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not to allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		service.delete(1L);
	}

}
