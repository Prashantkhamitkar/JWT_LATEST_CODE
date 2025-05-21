package com.jwt.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.model.User;
import com.jwt.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
private final UserRepository userRepository;
public UserDetailsServiceImpl(UserRepository userRepository) {
	this.userRepository=userRepository;
}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not found with the username "+username));
		
		return UserDetailsImpl.build(user);
	}

}
