package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.InGameAccount;
import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InGameAccountRepository extends CrudRepository<InGameAccount, Long> {
    public InGameAccount getByUserAndGame_Name(User user, String game_name);
}
