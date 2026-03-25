package com.memoir.accountbook.service;

import com.memoir.accountbook.FavoriteTransaction;
import com.memoir.accountbook.Member;
import com.memoir.accountbook.dto.FavoriteRequestDto;
import com.memoir.accountbook.dto.FavoriteResponseDto;
import com.memoir.accountbook.repository.FavoriteRepository;
import com.memoir.accountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    public List<FavoriteResponseDto> getFavorites(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        return favoriteRepository.findByMember(member).stream()
                .map(FavoriteResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createFavorite(String email, FavoriteRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        FavoriteTransaction favorite = FavoriteTransaction.builder()
                .member(member)
                .templateName(requestDto.getTemplateName())
                .type(requestDto.getType())
                .amount(requestDto.getAmount())
                .category(requestDto.getCategory())
                .build();

        return favoriteRepository.save(favorite).getId();
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
