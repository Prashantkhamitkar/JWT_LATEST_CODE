package com.jwt.security;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jwt.exception.TokenRefreshException;
import com.jwt.model.RefreshToken;
import com.jwt.repository.RefreshTokenRepository;
import com.jwt.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
	   @Value("${app.jwt.refreshExpirationMs}")
	    private Long refreshTokenDurationMs;
	   private final RefreshTokenRepository refreshTokenRepository;
	    private final UserRepository userRepository;
	    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,UserRepository userRepository) {
	    	this.userRepository=userRepository;
	    	this.refreshTokenRepository=refreshTokenRepository;
	    }
public Optional<RefreshToken> findByToken(String Token){
	return refreshTokenRepository.findByToken(Token);
}

@Transactional
public RefreshToken createRefreshToken(Long userId) {
    // First delete any existing token for this user
	
    deleteByUserId(userId);
    
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(userRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("User not found")));
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());
    
    return refreshTokenRepository.save(refreshToken);
}

public RefreshToken verifyExpiration (RefreshToken token) {
	if(token.getExpiryDate().compareTo(Instant.now())<0) {
		refreshTokenRepository.delete(token);
		 throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
	}
	return token;
}

@Transactional
public int deleteByUserId(Long UserId) {
	return refreshTokenRepository.deleteByUserId(UserId);
}

	    
}
