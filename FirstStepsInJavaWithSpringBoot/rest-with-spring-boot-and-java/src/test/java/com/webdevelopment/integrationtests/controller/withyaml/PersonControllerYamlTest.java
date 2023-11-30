package com.webdevelopment.integrationtests.controller.withyaml;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.webdevelopment.configs.TestConfigs;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.integrationtests.controller.withyaml.mapper.YmlMapper;
import com.webdevelopment.integrationtests.testcontainers.AbstractIntegrationTest;
import com.webdevelopment.integrationtests.vo.AccountCredentialsVO;
import com.webdevelopment.integrationtests.vo.PagedModelPerson;
import com.webdevelopment.integrationtests.vo.PersonVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YmlMapper objectMapper;
	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new YmlMapper();

		person = new PersonVO();

	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given()
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.basePath("/auth/signin").port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper).when().post().then().statusCode(200).extract()
				.body().as(TokenVO.class, objectMapper).getAccessToken();

		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {

		mockPerson();

		var persistedPerson = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper).when().post()
				.then().statusCode(201).extract().body().as(PersonVO.class, objectMapper);

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

		var persistedPerson = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper).when().post()
				.then().statusCode(201).extract().body().as(PersonVO.class, objectMapper);

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

		var persistedPerson = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId()).when()
				.patch("{id}").then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);

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

		var persistedPerson = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId()).when()
				.get("{id}").then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);

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
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId()).when()
				.delete("{id}").then().statusCode(204);
	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var wrapper = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParam("page", 3)
				.queryParam("size", 10)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.as(PagedModelPerson.class, objectMapper);
		
		List<PersonVO> people = wrapper.getContent();

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

		var wrapper = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("firstName", "ayr")
				.queryParam("page", 0)
				.queryParam("size", 6)
				.queryParam("direction", "asc")
				.when().get("findPersonByName/{firstName}")
				.then().statusCode(200).extract().body()
				.as(PagedModelPerson.class, objectMapper);
		
		List<PersonVO> people = wrapper.getContent();

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
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when().get()
				.then().statusCode(403);
	}
	
	@Test
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParam("page", 3)
				.queryParam("size", 10)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.asString();
		
		assertTrue(content.contains("- rel: \"first\"\n"
				+ "  href: \"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"prev\"\n"
				+ "  href: \"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"self\"\n"
				+ "  href: \"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\""));
		assertTrue(content.contains("- rel: \"next\"\n"
				+ "  href: \"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"last\"\n"
				+ "  href: \"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\""));
		
		assertTrue(content.contains("- rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/person/v1/658\""));
		assertTrue(content.contains("- rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/person/v1/102\""));
		assertTrue(content.contains("- rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/person/v1/440\""));
	}
	
	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasília - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);

	}

}
