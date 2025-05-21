package com.jwt.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwt.dto.SignupRequest;
import com.jwt.exception.EmailAlreadyExistsException;
import com.jwt.exception.UsernameAlreadyExistsException;
import com.jwt.model.ERole;
import com.jwt.model.Role;
import com.jwt.model.User;
import com.jwt.repository.RoleRepository;
import com.jwt.repository.UserRepository;

@Service
public class UserService {
	 private final UserRepository userRepository;
	    private final RoleRepository roleRepository;
	    private final PasswordEncoder passwordEncoder;

	    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.roleRepository = roleRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	    
	    @Transactional
	    public void registerUser(SignupRequest signUpRequest) {
	    	if(userRepository.existsByUsername(signUpRequest.getUsername())) {
	    		  throw new UsernameAlreadyExistsException("Error: Username is already taken!");
	    	}
	    	 if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	             throw new EmailAlreadyExistsException("Error: Email is already in use!");
	         }
	    	 
	    	 User user =new User(signUpRequest.getUsername(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));
	    	Set<String> strRoles=signUpRequest.getRoles();
	    	 Set<Role> roles=new HashSet<>();
	    	  if (strRoles == null) {
	              Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
	                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	              roles.add(userRole);
	          }else {
	              strRoles.forEach(role -> {
	                  switch (role) {
	                      case "admin":
	                          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                          roles.add(adminRole);
	                          break;
	                      case "teacher":
	                          Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
	                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                          roles.add(teacherRole);
	                          break;
	                      case "clerk":
	                          Role clerkRole = roleRepository.findByName(ERole.ROLE_CLERK)
	                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                          roles.add(clerkRole);
	                          break;
	                      default:
	                          Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
	                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                          roles.add(studentRole);
	                  }
	              });
	          }

	          user.setRoles(roles);
	          userRepository.save(user);
	      }
	  }
