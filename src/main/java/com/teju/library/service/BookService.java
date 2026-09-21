package com.teju.library.service;

import com.teju.library.exception.ResourceNotFoundException;
import com.teju.library.model.Book;
import com.teju.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Book updateBook(Long id, Book updated) {
        Book existing = getBookById(id);
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setIsbn(updated.getIsbn());
        existing.setCategory(updated.getCategory());

        int borrowed = existing.getTotalCopies() - existing.getAvailableCopies();
        existing.setTotalCopies(updated.getTotalCopies());
        existing.setAvailableCopies(Math.max(0, updated.getTotalCopies() - borrowed));

        return bookRepository.save(existing);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
