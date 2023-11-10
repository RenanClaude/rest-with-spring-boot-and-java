package com.webdevelopment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webdevelopment.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
