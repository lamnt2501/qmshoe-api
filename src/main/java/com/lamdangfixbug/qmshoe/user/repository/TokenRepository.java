package com.lamdangfixbug.qmshoe.user.repository;

import com.lamdangfixbug.qmshoe.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Query("select t from Token t where t.belongToUser = :belongsToUser and t.isRevoke = false")
    List<Token> findAllValidTokenByUser(String belongsToUser);
}
