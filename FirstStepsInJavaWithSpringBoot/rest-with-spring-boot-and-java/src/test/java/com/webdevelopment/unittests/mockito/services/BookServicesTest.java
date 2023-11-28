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

import com.webdevelopment.data.vo.v1.BookVO;
import com.webdevelopment.exceptions.RequiredObjectIsNullException;
import com.webdevelopment.model.entity.Book;
import com.webdevelopment.repositories.BookRepository;
import com.webdevelopment.services.BookServices;
import com.webdevelopment.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	BookServices service;
	@Mock
	BookRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book book = input.mockEntity(1);
		book.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		BookVO result = service.findById(1L);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(0.5, result.getPrice());
	}

//	@Test
//	void testFindAll() {
//		List<Book> list = input.mockEntityList();
//
//		when(repository.findAll()).thenReturn(list);
//
//		List<BookVO> booksVO = service.findAll();
//
//		assertNotNull(booksVO);
//		assertEquals(14, booksVO.size());
//		
//		BookVO firstBook = booksVO.get(1);
//
//		assertNotNull(firstBook);
//		assertNotNull(firstBook.getKey());
//		assertNotNull(firstBook.getLinks());
//		assertTrue(firstBook.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
//		assertEquals("Title test1", firstBook.getTitle());
//		assertEquals("Author Test1", firstBook.getAuthor());
//		assertNotNull(firstBook.getLaunchDate());
//		assertEquals(0.5, firstBook.getPrice());
//		
//		BookVO fourthBook = booksVO.get(4);
//
//		assertNotNull(fourthBook);
//		assertNotNull(fourthBook.getKey());
//		assertNotNull(fourthBook.getLinks());
//		assertTrue(fourthBook.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
//		assertEquals("Title test4", fourthBook.getTitle());
//		assertEquals("Author Test4", fourthBook.getAuthor());
//		assertNotNull(fourthBook.getLaunchDate());
//		assertEquals(0.5, fourthBook.getPrice());
//		
//		BookVO seventhBook = booksVO.get(7);
//
//		assertNotNull(seventhBook);
//		assertNotNull(seventhBook.getKey());
//		assertNotNull(seventhBook.getLinks());
//		assertTrue(seventhBook.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
//		assertEquals("Title test7", seventhBook.getTitle());
//		assertEquals("Author Test7", seventhBook.getAuthor());
//		assertNotNull(seventhBook.getLaunchDate());
//		assertEquals(0.5, seventhBook.getPrice());
//	}

	@Test
	void testCreate() {
		// Entity before repository
		Book book = input.mockEntity(1);

		// Entity after repository
		Book bookPersisted = book;
		bookPersisted.setId(1L);

		BookVO bookVO = input.mockVO(1);
		bookVO.setKey(1L);

		when(repository.save(book)).thenReturn(bookPersisted);

		BookVO result = service.create(bookVO);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(0.5, result.getPrice());
	}

	@Test
	void testCreateWithNullBook() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not to allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		Book book = input.mockEntity(1);
		book.setId(null);

		Book bookPersisted = book;
		bookPersisted.setId(1L);

		BookVO bookVO = input.mockVO(1);
		bookVO.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));
		when(repository.save(book)).thenReturn(bookPersisted);

		BookVO result = service.update(bookVO);

		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Title test1", result.getTitle());
		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(0.5, result.getPrice());
	}

	@Test
	void testUpdateWithNullBook() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not to allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Book book = input.mockEntity(1);
		book.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		service.delete(1L);
	}

}
