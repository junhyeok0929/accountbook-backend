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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    // 일별 거래 내역 조회
    @GetMapping("/daily")
    public ResponseEntity<List<TransactionResponseDto>> findMyDailyTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long memberId = 1L;
        List<TransactionResponseDto> transactions = transactionService.findMyDailyTransactions(memberId, date);
        return ResponseEntity.ok(transactions);
    }

    // 월별 거래 내역 조회
    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponseDto>> findMyMonthlyTransactions(
            @RequestParam int year,
            @RequestParam int month) {
        Long memberId = 1L; 
        List<TransactionResponseDto> transactions = transactionService.findMyMonthlyTransactions(memberId, year, month);
        return ResponseEntity.ok(transactions);
    }

    // 월별 요약 정보 (지출/수입 합계)
    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryResponseDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        Long memberId = 1L;
        MonthlySummaryResponseDto summary = transactionService.getMonthlySummary(memberId, year, month);
        return ResponseEntity.ok(summary);
    }

    // 새 거래 내역 및 일기 작성
    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateRequestDto requestDto) {
        Long memberId = 1L;
        transactionService.createTransaction(memberId, requestDto);
        return ResponseEntity.ok().build();
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
}
