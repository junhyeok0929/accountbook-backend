package com.memoir.accountbook.controller;

import com.memoir.accountbook.dto.FavoriteRequestDto;
import com.memoir.accountbook.dto.FavoriteResponseDto;
import com.memoir.accountbook.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<FavoriteResponseDto>> getFavorites() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<FavoriteResponseDto> favorites = favoriteService.getFavorites(email);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<Long> createFavorite(@RequestBody FavoriteRequestDto requestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long favoriteId = favoriteService.createFavorite(email, requestDto);
        return ResponseEntity.status(201).body(favoriteId);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok().build();
    }
}
