package com.teju.library.service;

import com.teju.library.exception.LibraryException;
import com.teju.library.exception.ResourceNotFoundException;
import com.teju.library.model.Book;
import com.teju.library.model.BorrowRecord;
import com.teju.library.model.Member;
import com.teju.library.repository.BookRepository;
import com.teju.library.repository.BorrowRecordRepository;
import com.teju.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BorrowService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         BookRepository bookRepository,
                         MemberRepository memberRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Issue a book to a member. Decrements the book's available copies.
     */
    public BorrowRecord borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (!book.isAvailable()) {
            throw new LibraryException("No copies available for book '" + book.getTitle() + "'.");
        }

        book.borrowCopy();
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord(book, member, LocalDate.now().plusDays(DEFAULT_LOAN_DAYS));
        return borrowRecordRepository.save(record);
    }

    /**
     * Return a borrowed book. Increments the book's available copies.
     */
    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + recordId));

        if (record.getStatus() != BorrowRecord.Status.BORROWED) {
            throw new LibraryException("This book has already been returned.");
        }

        record.markReturned();
        Book book = record.getBook();
        book.returnCopy();
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> getAllRecords() {
        return borrowRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BorrowRecord getRecordById(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> getRecordsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordRepository.findByStatus(BorrowRecord.Status.BORROWED)
                .stream()
                .filter(BorrowRecord::isOverdue)
                .toList();
    }
}
