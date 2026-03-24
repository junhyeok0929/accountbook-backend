package com.memoir.accountbook.controller;

// --- ▼▼▼ 필요한 import 구문들을 확인하거나 추가해주세요 ▼▼▼ ---
import com.memoir.accountbook.dto.LoginRequestDto;
import com.memoir.accountbook.dto.MemberSignUpRequestDto;
import com.memoir.accountbook.dto.TokenResponseDto;
import com.memoir.accountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// --- ▲▲▲ import 구문 확인 ▲▲▲ ---

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;

    // --- 1. 회원가입 API (POST /api/members/signup) (기존 코드) ---
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody MemberSignUpRequestDto requestDto) {
        memberService.signUp(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        TokenResponseDto tokenResponseDto = memberService.login(requestDto);
        return ResponseEntity.ok(tokenResponseDto);
    }
}