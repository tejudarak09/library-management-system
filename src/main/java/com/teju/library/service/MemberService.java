package com.teju.library.service;

import com.teju.library.exception.LibraryException;
import com.teju.library.exception.ResourceNotFoundException;
import com.teju.library.model.Member;
import com.teju.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(existing -> {
            throw new LibraryException("A member with email '" + member.getEmail() + "' already exists.");
        });
        if (member.getMembershipDate() == null) {
            member.setMembershipDate(LocalDate.now());
        }
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public Member updateMember(Long id, Member updated) {
        Member existing = getMemberById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        return memberRepository.save(existing);
    }

    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}
