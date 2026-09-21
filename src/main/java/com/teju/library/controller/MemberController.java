package com.teju.library.controller;

import com.teju.library.model.Member;
import com.teju.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    @GetMapping("/search")
    public List<Member> searchMembers(@RequestParam String name) {
        return memberService.searchByName(name);
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@Valid @RequestBody Member member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(member));
    }

    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
