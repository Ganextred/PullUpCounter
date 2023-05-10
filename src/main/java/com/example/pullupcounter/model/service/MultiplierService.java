package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.*;
import com.example.pullupcounter.data.repository.ExerciseRepository;
import com.example.pullupcounter.data.repository.MultiplierRepository;
import com.example.pullupcounter.data.repository.UserRepository;
import com.example.pullupcounter.model.enums.ExerciseName;
import com.example.pullupcounter.model.enums.GameName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MultiplierService {
    public static final double DEFAULT_MULTIPLIER = 2;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MultiplierRepository multiplierRepo;

    @Autowired
    ExerciseRepository exerciseRepo;

    @Autowired
    GameDriverFactory gameDriverFactory;

    public List<Multiplier> createMultipliers(User user, Game game) {
        List<Multiplier> list = new ArrayList<>();
        for (Exercise e : exerciseRepo.findAll()) {
            Multiplier multiplier = new Multiplier();
            multiplier.setMultiplier(DEFAULT_MULTIPLIER);
            multiplier.setExercise(e);
            multiplier.setGame(game);
            multiplier.setUser(user);
            multiplierRepo.save(multiplier);
        }
        return list;
    }
}
