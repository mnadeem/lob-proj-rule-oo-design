package com.org.lob.project.repository;

import com.org.lob.project.repository.entity.Book;

public interface BookRepository {

	Book getByIsbn(String isbn);
}
