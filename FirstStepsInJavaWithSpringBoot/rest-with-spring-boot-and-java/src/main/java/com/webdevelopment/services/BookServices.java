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

import com.webdevelopment.controllers.BookController;
import com.webdevelopment.data.vo.v1.BookVO;
import com.webdevelopment.exceptions.RequiredObjectIsNullException;
import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.mapper.DozerMapper;
import com.webdevelopment.model.entity.Book;
import com.webdevelopment.repositories.BookRepository;

@Service
public class BookServices {

	private BookRepository repository;
	private PagedResourcesAssembler<BookVO> assembler;

	@Autowired
	public BookServices(BookRepository personRepository, PagedResourcesAssembler<BookVO> assembler) {
		this.repository = personRepository;
		this.assembler = assembler;
	}

//	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(BookServices.class.getName());

	public BookVO findById(Long id) {
		logger.info("Finding one book");
		Book book = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		BookVO bookVO = DozerMapper.parseObject(book, BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return bookVO;
	}

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
		logger.info("Finding all books");
		
		Page<Book> bookPage = this.repository.findAll(pageable);
		
		Page<BookVO> bookVOsPage = bookPage.map(book -> DozerMapper.parseObject(book, BookVO.class));
		
		bookVOsPage.stream().forEach(
				book -> book.add(linkTo(methodOn(BookController.class).findById(book.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return this.assembler.toModel(bookVOsPage, link);
	}

	public BookVO create(BookVO bookVO) {

		if (bookVO == null)
			throw new RequiredObjectIsNullException();

		logger.info("Creating one book");

		Book book = DozerMapper.parseObject(bookVO, Book.class);
		Book bookCreated = repository.save(book);
		BookVO resBookVO = DozerMapper.parseObject(bookCreated, BookVO.class);
		resBookVO.add(linkTo(methodOn(BookController.class).findById(resBookVO.getKey())).withSelfRel());
		
		return resBookVO;
	}

	public BookVO update(BookVO bookVO) {

		if (bookVO == null)
			throw new RequiredObjectIsNullException();

		logger.info("Updating one book");

		Book bookToUpdate = repository.findById(bookVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		bookToUpdate.setTitle(bookVO.getTitle());
		bookToUpdate.setAuthor(bookVO.getAuthor());
		bookToUpdate.setLaunchDate(bookVO.getLaunchDate());
		bookToUpdate.setPrice(bookVO.getPrice());

		Book bookUpdated = repository.save(bookToUpdate);

		BookVO resBookVO = DozerMapper.parseObject(bookUpdated, BookVO.class);
		resBookVO.add(linkTo(methodOn(BookController.class).findById(resBookVO.getKey())).withSelfRel());
		return resBookVO;
	}

	public void delete(Long id) {
		logger.info("Deleting one book");

		Book bookToDelete = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(bookToDelete);
	}
}
