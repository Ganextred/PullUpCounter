package com.example.pullupcounter.data.repository;

import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    public User getUserByUsername(String username);

}
