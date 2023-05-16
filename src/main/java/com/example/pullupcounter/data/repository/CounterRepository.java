package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.Counter;
import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterRepository extends CrudRepository<Counter, Long> {
    public List<Counter> getByUser(User user);
}
