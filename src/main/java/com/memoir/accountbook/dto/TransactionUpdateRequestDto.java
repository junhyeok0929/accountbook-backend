package com.memoir.accountbook.dto;

import com.memoir.accountbook.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TransactionUpdateRequestDto {

    private LocalDate transactionDate;
    private TransactionType type;
    private Integer amount;
    private String category;
    
    // 일기 수정을 위한 필드 추가
    private String diaryTitle;
    private String diaryContent;
    private String imageUrl; // [추가]
}
