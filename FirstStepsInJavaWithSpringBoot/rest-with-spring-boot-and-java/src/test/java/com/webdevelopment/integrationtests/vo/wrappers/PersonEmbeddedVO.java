package com.webdevelopment.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webdevelopment.integrationtests.vo.PersonVO;

public class PersonEmbeddedVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("personVOList")
	private List<PersonVO> people;

	public PersonEmbeddedVO(List<PersonVO> people) {
		this.people = people;
	}

	public PersonEmbeddedVO() {
	}

	public List<PersonVO> getPeople() {
		return people;
	}

	public void setPeople(List<PersonVO> people) {
		this.people = people;
	}

	@Override
	public int hashCode() {
		return Objects.hash(people);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonEmbeddedVO other = (PersonEmbeddedVO) obj;
		return Objects.equals(people, other.people);
	}

}
