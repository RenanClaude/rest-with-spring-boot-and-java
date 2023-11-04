package com.webdevelopment.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.webdevelopment.data.vo.v2.PersonVOV2;
import com.webdevelopment.model.entity.Person;

@Service
public class PersonMapper {

	public PersonVOV2 convertEntityToVo(Person person) {

		PersonVOV2 personVO = new PersonVOV2();
		personVO.setId(person.getId());
		personVO.setAddress(person.getAddress());
		personVO.setBirthDay(new Date());
		personVO.setFirstName(person.getFirstName());
		personVO.setLastName(person.getLastName());
		personVO.setGender(person.getGender());

		return personVO;
	}

	public Person convertVoToEntity(PersonVOV2 personVO) {

		Person person = new Person();
		person.setId(personVO.getId());
		person.setAddress(personVO.getAddress());
//		person.setBirthDay(new Date());
		person.setFirstName(personVO.getFirstName());
		person.setLastName(personVO.getLastName());
		person.setGender(personVO.getGender());

		return person;
	}
}
