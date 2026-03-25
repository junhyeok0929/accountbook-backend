package com.memoir.accountbook.repository;

import com.memoir.accountbook.FavoriteTransaction;
import com.memoir.accountbook.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteTransaction, Long> {
    List<FavoriteTransaction> findByMember(Member member);
}
