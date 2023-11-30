package com.webdevelopment.integrationtests.controller.withyaml;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.webdevelopment.configs.TestConfigs;
import com.webdevelopment.data.vo.v1.security.TokenVO;
import com.webdevelopment.integrationtests.controller.withyaml.mapper.YmlMapper;
import com.webdevelopment.integrationtests.testcontainers.AbstractIntegrationTest;
import com.webdevelopment.integrationtests.vo.AccountCredentialsVO;
import com.webdevelopment.integrationtests.vo.BookVO;
import com.webdevelopment.integrationtests.vo.PagedModelBook;

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
public class BookControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YmlMapper objectMapper;
	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new YmlMapper();

		book = new BookVO();

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
				.setBasePath("/api/book/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {

		mockBook();

		var persistedBook = given()
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
				.body(book, objectMapper).when().post()
				.then().statusCode(201).extract().body().as(BookVO.class, objectMapper);

		 book = persistedBook;

		assertNotNull(book);

		assertNotNull(book.getId());
		assertNotNull(book.getTitle());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());

		assertTrue(book.getId() > 0);

		assertEquals("Estruturas de dados e algoritmos com JavaScript", book.getTitle());
		assertEquals("Loiane Groner", book.getAuthor());
		assertEquals(20.00, book.getPrice());
	}

	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {

		book.setPrice(30.00);

		var persistedBook = given()
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
				.body(book, objectMapper).when().put()
				.then().statusCode(201).extract().body().as(BookVO.class, objectMapper);

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

		var persistedBook = given()
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
				.pathParam("id", book.getId()).when()
				.get("{id}").then().statusCode(200).extract().body().as(BookVO.class, objectMapper);

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
				.config(
	                    RestAssuredConfig
	                        .config()
	                        .encoderConfig(EncoderConfig.encoderConfig()
	                            .encodeContentTypeAs(
	                                TestConfigs.CONTENT_TYPE_YML,
	                                ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getId()).when()
				.delete("{id}").then().statusCode(204);
	}
	
	@Test
	@Order(5)
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
				.queryParam("page", 0)
				.queryParam("size", 5)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.as(PagedModelBook.class, objectMapper);
		
		List<BookVO> books = wrapper.getContent();

		BookVO firstBookOnTheList = books.get(0);

		assertNotNull(firstBookOnTheList.getId());
		assertNotNull(firstBookOnTheList.getTitle());
		assertNotNull(firstBookOnTheList.getAuthor());
		assertNotNull(firstBookOnTheList.getLaunchDate());
		assertNotNull(firstBookOnTheList.getPrice());

		assertEquals(12, firstBookOnTheList.getId());

		assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", firstBookOnTheList.getTitle());
		assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", firstBookOnTheList.getAuthor());
		assertEquals(54.00, firstBookOnTheList.getPrice());
		
		BookVO fourthBookOnTheList = books.get(3);

		assertNotNull(fourthBookOnTheList.getId());
		assertNotNull(fourthBookOnTheList.getTitle());
		assertNotNull(fourthBookOnTheList.getAuthor());
		assertNotNull(fourthBookOnTheList.getLaunchDate());
		assertNotNull(fourthBookOnTheList.getPrice());

		assertEquals(2, fourthBookOnTheList.getId());

		assertEquals("Design Patterns", fourthBookOnTheList.getTitle());
		assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", fourthBookOnTheList.getAuthor());
		assertEquals(45.00, fourthBookOnTheList.getPrice());
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
	@Order(7)
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
				.queryParam("page", 1)
				.queryParam("size", 4)
				.queryParam("direction", "asc")
				.when().get()
				.then().statusCode(200).extract().body()
				.asString();
		
		assertTrue(content.contains("- rel: \"first\"\n"
				+ "  href: \"http://localhost:8888/api/book/v1?direction=asc&page=0&size=4&sort=title,asc\""));
		assertTrue(content.contains("- rel: \"prev\"\n"
				+ "  href: \"http://localhost:8888/api/book/v1?direction=asc&page=0&size=4&sort=title,asc\""));
		assertTrue(content.contains("- rel: \"self\"\n"
				+ "  href: \"http://localhost:8888/api/book/v1?page=1&size=4&direction=asc\""));
		assertTrue(content.contains("- rel: \"next\"\n"
				+ "  href: \"http://localhost:8888/api/book/v1?direction=asc&page=2&size=4&sort=title,asc\""));
		assertTrue(content.contains("- rel: \"last\"\n"
				+ "  href: \"http://localhost:8888/api/book/v1?direction=asc&page=3&size=4&sort=title,asc\""));
		
		assertTrue(content.contains("links:\n"
				+ "  - rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/book/v1/8\""));
		assertTrue(content.contains("links:\n"
				+ "  - rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/book/v1/11\""));
		assertTrue(content.contains("links:\n"
				+ "  - rel: \"self\"\n"
				+ "    href: \"http://localhost:8888/api/book/v1/7\""));
	}
	
	private void mockBook() {
		book.setTitle("Estruturas de dados e algoritmos com JavaScript");
		book.setAuthor("Loiane Groner");
		book.setLaunchDate(new Date());
		book.setPrice(20.00);

	}

}
