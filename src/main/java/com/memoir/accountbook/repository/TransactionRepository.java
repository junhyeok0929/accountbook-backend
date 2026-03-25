package com.memoir.accountbook.repository;
import java.util.List;
import com.memoir.accountbook.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByMember_Id(Long memberId);
    List<Transaction> findByMember_IdAndTransactionDate(Long memberId, LocalDate date);
    List<Transaction> findByMember_IdAndTransactionDateBetween(Long memberId, LocalDate from, LocalDate to);

    @Query("SELECT t FROM Transaction t LEFT JOIN t.diary d " +
           "WHERE t.member.id = :memberId AND (" +
           "t.category LIKE %:query% OR " +
           "d.title LIKE %:query% OR " +
           "d.content LIKE %:query%)")
    List<Transaction> searchByMemberAndQuery(@Param("memberId") Long memberId, @Param("query") String query);
}