package com.webdevelopment.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webdevelopment.configs.TestConfigs;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.integrationtests.testcontainers.AbstractIntegrationTest;
import com.webdevelopment.integrationtests.vo.AccountCredentialsVO;
import com.webdevelopment.integrationtests.vo.PersonVO;
import com.webdevelopment.integrationtests.vo.wrappers.WrapperPersonVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();

	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given().basePath("/auth/signin").port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON).body(user).when().post().then().statusCode(200).extract()
				.body().as(TokenVO.class).getAccessToken();

		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {

		mockPerson();

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person).when().post()
				.then().statusCode(201).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {

		person.setLastName("Piquet Souto Maior");

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person).when().post()
				.then().statusCode(201).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	

	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId()).when()
				.patch("{id}").then().statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	
	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {

		mockPerson();

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId()).when()
				.get("{id}").then().statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

				given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", person.getId()).when()
				.delete("{id}").then().statusCode(204);
	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParam("page", 3)
				.queryParam("size", 10)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		List<PersonVO> people = wrapper.getEmbedded().getPeople();

		PersonVO firstPersonOnTheList = people.get(0);

		assertNotNull(firstPersonOnTheList.getId());
		assertNotNull(firstPersonOnTheList.getFirstName());
		assertNotNull(firstPersonOnTheList.getLastName());
		assertNotNull(firstPersonOnTheList.getAddress());
		assertNotNull(firstPersonOnTheList.getGender());
		
		assertTrue(firstPersonOnTheList.getEnabled());

		assertEquals(658, firstPersonOnTheList.getId());

		assertEquals("Amil", firstPersonOnTheList.getFirstName());
		assertEquals("Boteman", firstPersonOnTheList.getLastName());
		assertEquals("12 Meadow Vale Hill", firstPersonOnTheList.getAddress());
		assertEquals("Female", firstPersonOnTheList.getGender());
		
		PersonVO sixthPersonOnTheList = people.get(5);

		assertNotNull(sixthPersonOnTheList.getId());
		assertNotNull(sixthPersonOnTheList.getFirstName());
		assertNotNull(sixthPersonOnTheList.getLastName());
		assertNotNull(sixthPersonOnTheList.getAddress());
		assertNotNull(sixthPersonOnTheList.getGender());
		
		assertFalse(sixthPersonOnTheList.getEnabled());

		assertEquals(989, sixthPersonOnTheList.getId());

		assertEquals("Andrey", sixthPersonOnTheList.getFirstName());
		assertEquals("Wipper", sixthPersonOnTheList.getLastName());
		assertEquals("7 Sloan Road", sixthPersonOnTheList.getAddress());
		assertEquals("Male", sixthPersonOnTheList.getGender());
	}
	
	@Test
	@Order(7)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "ayr")
				.queryParam("page", 0)
				.queryParam("size", 6)
				.queryParam("direction", "asc")
				.when().get("findPersonByName/{firstName}")
				.then().statusCode(200).extract().body()
				.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		List<PersonVO> people = wrapper.getEmbedded().getPeople();

		PersonVO firstPersonOnTheList = people.get(0);

		assertNotNull(firstPersonOnTheList.getId());
		assertNotNull(firstPersonOnTheList.getFirstName());
		assertNotNull(firstPersonOnTheList.getLastName());
		assertNotNull(firstPersonOnTheList.getAddress());
		assertNotNull(firstPersonOnTheList.getGender());
		
		assertTrue(firstPersonOnTheList.getEnabled());

		assertEquals(1, firstPersonOnTheList.getId());

		assertEquals("Ayrton", firstPersonOnTheList.getFirstName());
		assertEquals("Senna", firstPersonOnTheList.getLastName());
		assertEquals("São Paulo", firstPersonOnTheList.getAddress());
		assertEquals("Male", firstPersonOnTheList.getGender());
	}
	
	@Test
	@Order(8)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
				given()
				.spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when().get()
				.then().statusCode(403);
	}
	
	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParam("page", 3)
				.queryParam("size", 10)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.asString();
		
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/658\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/102\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/440\"}}}"));
		
		assertTrue(content.contains("\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));
		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1007,\"totalPages\":101,\"number\":3}}"));
	}
	
	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasília - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);

	}

}
