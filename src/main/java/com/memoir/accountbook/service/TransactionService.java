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
    public Long createTransaction(String email, TransactionCreateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. (이메일: " + email + ")"));

        // 1. 일기(Diary) 데이터가 있을 때만 객체 생성
        Diary diary = null;
        if (requestDto.getDiaryContent() != null && !requestDto.getDiaryContent().trim().isEmpty()) {
            diary = Diary.builder()
                    .title(requestDto.getDiaryTitle())
                    .content(requestDto.getDiaryContent())
                    .imageUrl(requestDto.getImageUrl()) // [추가]
                    .build();
            // (참고) @PrePersist에 의해 createdAt은 자동 생성됩니다.
        }

        // 2. 가계부(Transaction) 객체 생성 및 일기 연결
        Transaction transaction = Transaction.builder()
                .member(member)
                .transactionDate(requestDto.getTransactionDate())
                .type(requestDto.getType())
                .amount(requestDto.getAmount())
                .category(requestDto.getCategory())
                .diary(diary) // CascadeType.ALL에 의해 diary도 같이 저장됨
                .build();

        // 3. DB 저장 및 ID 반환
        return transactionRepository.save(transaction).getId();
    }

    // 월별 요약 조회 (카테고리별 통계 포함)
    public MonthlySummaryResponseDto getMonthlySummary(String email, int year, int month) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Long memberId = member.getId();

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
    public List<TransactionResponseDto> findMyMonthlyTransactions(String email, int year, int month) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Long memberId = member.getId();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return transactionRepository.findByMember_IdAndTransactionDateBetween(memberId, start, end).stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> searchTransactions(String email, String query) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        return transactionRepository.searchByMemberAndQuery(member.getId(), query).stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTransaction(String email, Long transactionId, TransactionUpdateRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역을 찾을 수 없습니다."));

        // 보안 검증: 현재 로그인한 사용자가 이 거래 내역의 주인인지 확인
        if (!transaction.getMember().getEmail().equals(email)) {
            throw new RuntimeException("해당 거래 내역을 수정할 권한이 없습니다.");
        }

        Diary diary = transaction.getDiary();
        if (requestDto.getDiaryContent() != null && !requestDto.getDiaryContent().trim().isEmpty()) {
            if (diary == null) {
                diary = Diary.builder()
                        .title(requestDto.getDiaryTitle())
                        .content(requestDto.getDiaryContent())
                        .imageUrl(requestDto.getImageUrl())
                        .build();
            } else {
                diary.update(requestDto.getDiaryTitle(), requestDto.getDiaryContent(), requestDto.getImageUrl());
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
    public void deleteTransaction(String email, Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역을 찾을 수 없습니다."));

        // 보안 검증: 현재 로그인한 사용자가 이 거래 내역의 주인인지 확인
        if (!transaction.getMember().getEmail().equals(email)) {
            throw new RuntimeException("해당 거래 내역을 삭제할 권한이 없습니다.");
        }

        transactionRepository.delete(transaction);
    }

    public List<TransactionResponseDto> findMyDailyTransactions(String email, LocalDate date) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Long memberId = member.getId();

        return transactionRepository.findByMember_IdAndTransactionDate(memberId, date).stream()
                .map(TransactionResponseDto::new)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto findMyTransactionById(String email, Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("거래 내역을 찾을 수 없습니다."));

        // 보안 검증: 현재 로그인한 사용자가 이 거래 내역의 주인인지 확인
        if (!transaction.getMember().getEmail().equals(email)) {
            throw new RuntimeException("해당 거래 내역을 조회할 권한이 없습니다.");
        }

        return new TransactionResponseDto(transaction);
    }
}
