package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.MonthlySummaryResponseDto;
import com.memoir.accountbook.dto.TransactionCreateRequestDto;
import com.memoir.accountbook.dto.TransactionResponseDto;
import com.memoir.accountbook.dto.TransactionUpdateRequestDto;
import com.memoir.accountbook.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class TransactionController {

    private final TransactionService transactionService;

    // 일별 거래 내역 조회
    @GetMapping("/daily")
    public ResponseEntity<List<TransactionResponseDto>> findMyDailyTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<TransactionResponseDto> transactions = transactionService.findMyDailyTransactions(email, date);
        return ResponseEntity.ok(transactions);
    }

    // 월별 거래 내역 조회
    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponseDto>> findMyMonthlyTransactions(
            @RequestParam int year,
            @RequestParam int month) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<TransactionResponseDto> transactions = transactionService.findMyMonthlyTransactions(email, year, month);
        return ResponseEntity.ok(transactions);
    }

    // 월별 요약 정보 (지출/수입 합계)
    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryResponseDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        MonthlySummaryResponseDto summary = transactionService.getMonthlySummary(email, year, month);
        return ResponseEntity.ok(summary);
    }

    // 새 거래 내역 및 일기 작성
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionCreateRequestDto requestDto) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(401).body("로그인이 필요합니다.");
            }
            String email = auth.getName(); 
            Long transactionId = transactionService.createTransaction(email, requestDto);
            return ResponseEntity.status(201).body(transactionId);
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 에러 출력
            return ResponseEntity.status(500).body("저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 특정 거래 내역 상세 조회
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> findMyTransactionById(@PathVariable Long transactionId) {
        TransactionResponseDto transaction = transactionService.findMyTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    // 거래 내역 및 일기 수정
    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequestDto requestDto) {
        transactionService.updateTransaction(transactionId, requestDto);
        return ResponseEntity.ok().build();
    }

    // 거래 내역 및 일기 삭제
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }

    // 거래 내역 및 일기 검색
    @GetMapping("/search")
    public ResponseEntity<List<TransactionResponseDto>> searchTransactions(@RequestParam String query) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<TransactionResponseDto> results = transactionService.searchTransactions(email, query);
        return ResponseEntity.ok(results);
    }
}
