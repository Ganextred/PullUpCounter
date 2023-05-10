package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.Counter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends CrudRepository<Counter, Long> {
    //public List<Counter> getByUser(String user_username, String game_name, String exercise_name);
}
