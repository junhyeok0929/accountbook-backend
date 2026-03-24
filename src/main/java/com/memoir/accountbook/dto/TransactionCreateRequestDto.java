package com.memoir.accountbook.dto;

import com.memoir.accountbook.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TransactionCreateRequestDto {
    private LocalDate transactionDate;
    private TransactionType type;
    private Integer amount;
    private String category;
    
    // 일기 데이터 추가
    private String diaryTitle;
    private String diaryContent;
    private String imageUrl; // 나중에 사진 업로드를 위해 미리 추가
}
