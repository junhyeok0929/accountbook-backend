package com.memoir.accountbook.service;

import com.memoir.accountbook.*;
import com.memoir.accountbook.dto.*;
import com.memoir.accountbook.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createTransaction(Long memberId, TransactionCreateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Diary diary = null;
        if (requestDto.getDiaryContent() != null && !requestDto.getDiaryContent().trim().isEmpty()) {
            diary = Diary.builder()
                    .title(requestDto.getDiaryTitle())
                    .content(requestDto.getDiaryContent())
                    .build();
        }

        Transaction transaction = Transaction.builder()
                .member(member)
                .transactionDate(requestDto.getTransactionDate())
                .type(requestDto.getType())
                .amount(requestDto.getAmount())
                .category(requestDto.getCategory())
                .diary(diary)
                .build();

        return transactionRepository.save(transaction).getId();
    }

    // 월별 요약 조회 (카테고리별 통계 포함)
    public MonthlySummaryResponseDto getMonthlySummary(Long memberId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByMember_IdAndTransactionDateBetween(memberId, start, end);

        // 총합 계산
        long totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToLong(Transaction::getAmount)
                .sum();

        long totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToLong(Transaction::getAmount)
                .sum();

        // 카테고리별 지출 통계 계산
        Map<String, Long> expenseByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingLong(Transaction::getAmount)));

        // 카테고리별 수입 통계 계산
        Map<String, Long> incomeByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingLong(Transaction::getAmount)));

        // Builder를 사용하여 DTO 생성 (컴파일 에러 해결)
        return MonthlySummaryResponseDto.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .incomeByCategory(incomeByCategory)
                .expenseByCategory(expenseByCategory)
                .build();
    }

    // 월별 상세 거래 목록 조회
    public List<TransactionResponseDto> findMyMonthlyTransactions(Long memberId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return transactionRepository.findByMember_IdAndTransactionDateBetween(memberId, start, end).stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTransaction(Long transactionId, TransactionUpdateRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역을 찾을 수 없습니다."));

        Diary diary = transaction.getDiary();
        if (requestDto.getDiaryContent() != null && !requestDto.getDiaryContent().trim().isEmpty()) {
            if (diary == null) {
                diary = Diary.builder()
                        .title(requestDto.getDiaryTitle())
                        .content(requestDto.getDiaryContent())
                        .build();
            } else {
                diary.update(requestDto.getDiaryTitle(), requestDto.getDiaryContent());
            }
        } else {
            diary = null;
        }

        transaction.update(
                requestDto.getTransactionDate(),
                requestDto.getType(),
                requestDto.getAmount(),
                requestDto.getCategory(),
                diary
        );
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<TransactionResponseDto> findMyDailyTransactions(Long memberId, LocalDate date) {
        return transactionRepository.findByMember_IdAndTransactionDate(memberId, date).stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto findMyTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역을 찾을 수 없습니다."));
    }
}
