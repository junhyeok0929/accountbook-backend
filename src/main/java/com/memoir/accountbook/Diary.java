package com.memoir.accountbook;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 일기 내용

    private String title; // 일기 제목 (선택 사항)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "diary")
    private Transaction transaction;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
