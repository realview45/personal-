package com.example.chatserver.member.service;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(MemberSaveReqDto memberSaveReqDto) {
        //중복이메일체크
        Optional<Member> member = memberRepository.findByEmail(memberSaveReqDto.getEmail());
        if(member.isPresent()) throw new EntityNotFoundException("이메일이 중복입니다.");
        Member newMember = Member.builder()
                .email(memberSaveReqDto.getEmail())
                .name(memberSaveReqDto.getName())
                .password(memberSaveReqDto.getPassword())
                .build();
        memberRepository.save(newMember);
        return newMember;
    }
}
