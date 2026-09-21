package com.teju.library.controller;

import com.teju.library.model.BorrowRecord;
import com.teju.library.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<BorrowRecord> getAllRecords() {
        return borrowService.getAllRecords();
    }

    @GetMapping("/{id}")
    public BorrowRecord getRecordById(@PathVariable Long id) {
        return borrowService.getRecordById(id);
    }

    @GetMapping("/member/{memberId}")
    public List<BorrowRecord> getRecordsByMember(@PathVariable Long memberId) {
        return borrowService.getRecordsByMember(memberId);
    }

    @GetMapping("/overdue")
    public List<BorrowRecord> getOverdueRecords() {
        return borrowService.getOverdueRecords();
    }

    @PostMapping
    public ResponseEntity<BorrowRecord> borrowBook(@RequestBody Map<String, Long> payload) {
        Long bookId = payload.get("bookId");
        Long memberId = payload.get("memberId");
        BorrowRecord record = borrowService.borrowBook(bookId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/{id}/return")
    public BorrowRecord returnBook(@PathVariable Long id) {
        return borrowService.returnBook(id);
    }
}
