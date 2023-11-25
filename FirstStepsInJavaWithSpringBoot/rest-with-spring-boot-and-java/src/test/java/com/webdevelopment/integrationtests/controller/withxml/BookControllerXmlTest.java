package com.webdevelopment.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webdevelopment.configs.TestConfigs;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.integrationtests.testcontainers.AbstractIntegrationTest;
import com.webdevelopment.integrationtests.vo.AccountCredentialsVO;
import com.webdevelopment.integrationtests.vo.BookVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookVO();

	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given().basePath("/auth/signin").port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON).body(user).when().post().then().statusCode(200).extract()
				.body().as(TokenVO.class).getAccessToken();

		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {

		mockBook();

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book).when().post()
				.then().statusCode(201).extract().body().asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());

		assertTrue(persistedBook.getId() > 0);

		assertEquals("Estruturas de dados e algoritmos com JavaScript", persistedBook.getTitle());
		assertEquals("Loiane Groner", persistedBook.getAuthor());
		assertEquals(20.00, persistedBook.getPrice());
	}

	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {

		book.setPrice(30.00);

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book).when().post()
				.then().statusCode(201).extract().body().asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());

		assertEquals(book.getId(), persistedBook.getId());

		assertEquals("Estruturas de dados e algoritmos com JavaScript", persistedBook.getTitle());
		assertEquals("Loiane Groner", persistedBook.getAuthor());
		assertEquals(30.00, persistedBook.getPrice());
	}
	

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {

		mockBook();

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", book.getId()).when()
				.get("{id}").then().statusCode(200).extract().body().asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());

		assertEquals(book.getId(), persistedBook.getId());

		assertEquals("Estruturas de dados e algoritmos com JavaScript", persistedBook.getTitle());
		assertEquals("Loiane Groner", persistedBook.getAuthor());
		assertEquals(30.00, persistedBook.getPrice());
	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

				given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", book.getId()).when()
				.delete("{id}").then().statusCode(204);
	}
	
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 0 , "limit", 5, "direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.asString();
//				.as(new TypeRef<List<BookVO>>() {});
		
		List<BookVO> books = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});

		BookVO firstBookOnTheList = books.get(0);

		assertNotNull(firstBookOnTheList.getId());
		assertNotNull(firstBookOnTheList.getTitle());
		assertNotNull(firstBookOnTheList.getAuthor());
		assertNotNull(firstBookOnTheList.getLaunchDate());
		assertNotNull(firstBookOnTheList.getPrice());

		assertEquals(1, firstBookOnTheList.getId());

		assertEquals("Working effectively with legacy code", firstBookOnTheList.getTitle());
		assertEquals("Michael C. Feathers", firstBookOnTheList.getAuthor());
		assertEquals(49.00, firstBookOnTheList.getPrice());
		
		BookVO sixthBookOnTheList = books.get(5);

		assertNotNull(sixthBookOnTheList.getId());
		assertNotNull(sixthBookOnTheList.getTitle());
		assertNotNull(sixthBookOnTheList.getAuthor());
		assertNotNull(sixthBookOnTheList.getLaunchDate());
		assertNotNull(sixthBookOnTheList.getPrice());

		assertEquals(6, sixthBookOnTheList.getId());

		assertEquals("Refactoring", sixthBookOnTheList.getTitle());
		assertEquals("Martin Fowler e Kent Beck", sixthBookOnTheList.getAuthor());
		assertEquals(88.00, sixthBookOnTheList.getPrice());
	}
	
	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
				given()
				.spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when().get()
				.then().statusCode(403);
	}
	
	private void mockBook() {
		book.setTitle("Estruturas de dados e algoritmos com JavaScript");
		book.setAuthor("Loiane Groner");
		book.setLaunchDate(new Date());
		book.setPrice(20.00);

	}

}
