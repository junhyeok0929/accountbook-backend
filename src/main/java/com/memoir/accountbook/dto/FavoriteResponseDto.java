package com.memoir.accountbook.dto;

import com.memoir.accountbook.FavoriteTransaction;
import com.memoir.accountbook.TransactionType;
import lombok.Getter;

@Getter
public class FavoriteResponseDto {
    private Long favoriteId;
    private String templateName;
    private TransactionType type;
    private Integer amount;
    private String category;

    public FavoriteResponseDto(FavoriteTransaction favorite) {
        this.favoriteId = favorite.getId();
        this.templateName = favorite.getTemplateName();
        this.type = favorite.getType();
        this.amount = favorite.getAmount();
        this.category = favorite.getCategory();
    }
}
