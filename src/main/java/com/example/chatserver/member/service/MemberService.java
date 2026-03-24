package com.example.chatserver.member.service;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberSaveReqDto memberSaveReqDto) {
        //중복이메일체크
        Optional<Member> member = memberRepository.findByEmail(memberSaveReqDto.getEmail());
        if(member.isPresent()) {
            throw new EntityNotFoundException("이메일이 중복입니다.");
        }
        Member newMember = Member.builder()
                .email(memberSaveReqDto.getEmail())
                .name(memberSaveReqDto.getName())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword()))
                .build();
        memberRepository.save(newMember);
        return newMember;
    }

    public Member doLogin(MemberLoginReqDto memberLoginReqDto) {
        Optional<Member> member = memberRepository.findByEmail(memberLoginReqDto.getEmail());
        boolean login = true;
        if(member.isPresent()){
            if(!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.get().getPassword())) {
                login = false;
            }
        }
        else{
            login=false;
        }
        if(!login){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return member.get();
    }
}
