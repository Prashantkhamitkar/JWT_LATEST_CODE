package com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jwt.model.RefreshToken;
import com.jwt.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);
	
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
	
	int deleteByUserId(@Param("userId") Long userId);

}
