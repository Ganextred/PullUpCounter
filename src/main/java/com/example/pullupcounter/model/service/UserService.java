package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.*;
import com.example.pullupcounter.data.repository.GameRepository;
import com.example.pullupcounter.data.repository.InGameAccountRepository;
import com.example.pullupcounter.data.repository.MultiplierRepository;
import com.example.pullupcounter.data.repository.UserRepository;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.ExerciseName;
import com.example.pullupcounter.model.enums.GameName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    MultiplierRepository multiplierRepo;

    @Autowired
    InGameAccountRepository lastMatchRepo;

    @Autowired
    GameDriverFactory gameDriverFactory;

    @Autowired
    CounterService counterService;
    @Autowired
    MultiplierService multiplierService;

    @Autowired
    GameRepository gameRepository;

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

    public Multiplier setMultiplier(User user, ExerciseName exercise, GameName game, Double multiplier) {
        Multiplier multiplierEntity =
                multiplierRepo.getByUser_UsernameAndGame_NameAndExercise_Name(user.getUsername(), exercise.name(), game.name());
        if (multiplierEntity == null)
            return null;
        multiplierEntity.setMultiplier(multiplier);
        return multiplierRepo.save(multiplierEntity);
    }

    public Integer updateGameHistory(User user, GameName game) throws ApiAccessException {
        InGameAccount inGameAccount = lastMatchRepo.getByUserAndGame_Name(user, game.name());

        GameDriver driver = gameDriverFactory.findStrategy(game);
        Integer deaths = driver.getDeaths(inGameAccount);


        user.getCounters().forEach((counter -> {
            Multiplier multiplier = multiplierRepo.getByUserAndGame_NameAndExercise(user, game.name(), counter.getExercise());
            counterService.increaseCounter(counter, deaths * multiplier.getMultiplier());
        }
        ));
        return deaths;
    }

    public InGameAccount bindInGameAccount(User user, String inGameAccountName, GameName game) throws ApiAccessException {
        GameDriver driver = gameDriverFactory.findStrategy(game);
        InGameAccount inGameAccount = driver.bindAccount(inGameAccountName, user);
        if (inGameAccount != null) {
            multiplierService.createMultipliers(user, gameRepository.getGameByName(game.name()));
        }
        return inGameAccount;
    }

    public void test() throws JsonProcessingException {
        InGameAccount account = new InGameAccount();
        account.setPuuid("nhDrRwRutc1iTOtWwgOAjLi_nQSPfaTfYWdiWejU3X5eBehiAVx1uUEKyLbgUyFBkfLPTEfY-ZjBPw");

        String GET_MATCHES_URI = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?api_key={api_key}";

        RestTemplate restTemplate = new RestTemplate();

        String jsonString = restTemplate.getForObject(GET_MATCHES_URI,
                String.class,
                account.getPuuid(),
                "RGAPI-e1c96afc-6037-4301-a732-b8eb3afe92c5"
        );
        System.out.println(jsonString);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.readValue(jsonString, new TypeReference<List<String>>() {
        }));
    }
}
