package com.webdevelopment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webdevelopment.data.vo.v1.BookVO;
import com.webdevelopment.services.BookServices;
import com.webdevelopment.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints for Managing books")
public class BookController {

//	private final AtomicLong counter = new AtomicLong();

	private BookServices service;

	@Autowired
	public BookController(BookServices service) {
		this.service = service;
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	@Operation(summary = "Finds a Book", description = "Finds a Book", tags = { "Books" },
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content =
					@Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<BookVO> findById(@PathVariable(value = "id") Long id) {
		BookVO bookVO = this.service.findById(id);

		return ResponseEntity.status(HttpStatus.OK).body(bookVO);

	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Finds all books", description = "Finds all books", tags = { "Books" },
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<PagedModel<EntityModel<BookVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
		
		PagedModel<EntityModel<BookVO>> books = this.service.findAll(pageable);

		return ResponseEntity.status(HttpStatus.OK).body(books);
	}

	@PostMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@Operation(summary = "Adds a new Book", description = "Adds a new Book by passing in a JSON, XML, or YML representation of the Book", tags = { "Books" },
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", content =
					@Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<BookVO> create(@RequestBody BookVO bookVO) {
		BookVO bookCreated = this.service.create(bookVO);

		return ResponseEntity.status(HttpStatus.CREATED).body(bookCreated);
	}

	@PutMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@Operation(summary = "Updates a Book", description = "Updates a Book by passing in a JSON, XML, or YML representation of the Book", tags = { "Books" },
	responses = {
			@ApiResponse(description = "Updated", responseCode = "200", content =
					@Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<BookVO> update(@RequestBody BookVO bookVO) {
		BookVO bookUpdated = this.service.update(bookVO);

		return ResponseEntity.status(HttpStatus.CREATED).body(bookUpdated);

	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a Book", description = "Deletes a Book by passing in a JSON, XML, or YML representation of the Book", tags = { "Books" },
	responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			})
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		this.service.delete(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
