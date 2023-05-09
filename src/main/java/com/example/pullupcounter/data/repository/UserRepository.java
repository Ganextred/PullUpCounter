package com.example.pullupcounter.data.repository;

import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

	public User getUserByUsername(String username);
}
