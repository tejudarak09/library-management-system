package com.teju.library.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    public enum Status {
        BORROWED,
        RETURNED,
        OVERDUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate borrowDate = LocalDate.now();

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.BORROWED;

    public BorrowRecord() {
    }

    public BorrowRecord(Book book, Member member, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = LocalDate.now();
        this.dueDate = dueDate;
        this.status = Status.BORROWED;
    }

    // ---- business helpers ----
    public boolean isOverdue() {
        return status == Status.BORROWED && dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
        this.status = Status.RETURNED;
    }

    // ---- getters & setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
