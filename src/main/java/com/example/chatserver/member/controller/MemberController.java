package com.example.chatserver.member.controller;

import com.example.chatserver.common.auth.JwtTokenProvider;
import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListResDto;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Controller + 모든 메서드에 RequestBody가 붙음
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto){
        Member member = memberService.create(memberSaveReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(member.getId());
    }
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDto memberLoginReqDto){
//  email, password 검증
        Member member =memberService.doLogin(memberLoginReqDto);
//  일치할 경우, accessToken발행
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token",jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(loginInfo);
    }
    @GetMapping("/list")
    public ResponseEntity<?> memberList(){
        List<MemberListResDto> dtos = memberService.findAll();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
