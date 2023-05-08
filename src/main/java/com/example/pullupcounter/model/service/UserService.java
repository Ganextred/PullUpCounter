package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public void processOAuthPostLogin(String username) {
		User existUser = repo.getUserByUsername(username);
		
		if (existUser == null) {
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setEnabled(true);
			
			repo.save(newUser);
			
			System.out.println("Created new user: " + username);
		}
		
	}
	
}
