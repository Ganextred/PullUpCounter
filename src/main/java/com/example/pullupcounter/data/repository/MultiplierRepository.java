package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.Exercise;
import com.example.pullupcounter.data.entity.Game;
import com.example.pullupcounter.data.entity.Multiplier;
import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultiplierRepository extends CrudRepository<Multiplier, Long> {
    public Multiplier getByUser_UsernameAndGame_NameAndExercise_Name(String user_username, String game_name, String exercise_name);

    public Multiplier getByUserAndGame_NameAndExercise(User user, String game_name, Exercise exercise);
    public List<Multiplier> getByUserAndGame(User user, Game game);
}
