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

    @GetMapping("/daily")
    public ResponseEntity<List<TransactionResponseDto>> findMyDailyTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TransactionResponseDto> transactions = transactionService.findMyDailyTransactions(date);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponseDto>> findMyMonthlyTransactions(
            @RequestParam int year,
            @RequestParam int month) {
        List<TransactionResponseDto> transactions = transactionService.findMyMonthlyTransactions(year, month);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryResponseDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        MonthlySummaryResponseDto summary = transactionService.getMonthlySummary(year, month);
        return ResponseEntity.ok(summary);
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionCreateRequestDto requestDto) {
        transactionService.createTransaction(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<TransactionResponseDto>> findMyTransactions() {
        List<TransactionResponseDto> transactions = transactionService.findMyTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> findMyTransactionById(@PathVariable Long transactionId) {
        TransactionResponseDto transaction = transactionService.findMyTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionUpdateRequestDto requestDto) {

        transactionService.updateTransaction(transactionId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {

        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}
