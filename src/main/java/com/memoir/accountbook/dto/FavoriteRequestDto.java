package com.memoir.accountbook.dto;

import com.memoir.accountbook.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {
    private String templateName;
    private TransactionType type;
    private Integer amount;
    private String category;
}
