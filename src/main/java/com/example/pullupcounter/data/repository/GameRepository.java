package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    public Game getGameByName(String game_name);
}
