package com.memoir.accountbook.dto;

import com.memoir.accountbook.Transaction;
import com.memoir.accountbook.TransactionType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransactionResponseDto {
    private Long transactionId;
    private LocalDate transactionDate;
    private TransactionType type;
    private Integer amount;
    private String category;
    
    // 일기 데이터 포함
    private String diaryTitle;
    private String diaryContent;
    private String imageUrl;

    public TransactionResponseDto(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.transactionDate = transaction.getTransactionDate();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.category = transaction.getCategory();
        
        if (transaction.getDiary() != null) {
            this.diaryTitle = transaction.getDiary().getTitle();
            this.diaryContent = transaction.getDiary().getContent();
        }
    }
}
