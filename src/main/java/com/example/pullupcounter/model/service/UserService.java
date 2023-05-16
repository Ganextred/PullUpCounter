package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.*;
import com.example.pullupcounter.data.repository.*;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.GameName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Double DEFAULT_MULTIPLIER = 2.0;
    private static final Double DEFAULT_COUNTER = 0.0;

    @Autowired
    private UserRepository repo;

    @Autowired
    private MultiplierRepository multiplierRepo;

    @Autowired
    private InGameAccountRepository lastMatchRepo;

    @Autowired
    private GameDriverFactory gameDriverFactory;

    @Autowired
    private CounterService counterService;
    @Autowired
    private MultiplierService multiplierService;
    @Autowired
    private ExerciseRepository exerciseRepo;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private InGameAccountRepository inGameAccountRepository;
    @Autowired
    private CounterRepository counterRepository;

    public void processOAuthPostLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEnabled(true);
            List<Multiplier> multipliers = new ArrayList<>();
            List<Counter> counters = new ArrayList<>();
            for (Exercise exercise : exerciseRepo.findAll()) {
                for (Game game : gameRepository.findAll()) {
                    Multiplier multiplier = new Multiplier();
                    multiplier.setUser(newUser);
                    multiplier.setMultiplier(DEFAULT_MULTIPLIER);
                    multiplier.setGame(game);
                    multiplier.setExercise(exercise);
                    multipliers.add(multiplier);
                }
                Counter counter = new Counter();
                counter.setCounter(DEFAULT_COUNTER);
                counter.setUser(newUser);
                counter.setExercise(exercise);
                counters.add(counter);
            }
            newUser.setMultipliers(multipliers);
            newUser.setCounters(counters);
            repo.save(newUser);
            System.out.println("Created new user: " + username);
        }

    }

    public Multiplier setMultiplier(User user, String exercise, GameName game, Double multiplier) {
        Multiplier multiplierEntity =
                multiplierRepo.getByUser_UsernameAndGame_NameAndExercise_Name(user.getUsername(), game.name(), exercise);
        if (multiplierEntity == null)
            return null;
        multiplierEntity.setMultiplier(multiplier);
        return multiplierRepo.save(multiplierEntity);
    }

    public List<String> updateAllCounters() throws ApiAccessException {
        Iterable<User> users = userRepo.findAll();
        List<String> message = new ArrayList<>();
        for (User user : users) {
            Hibernate.initialize(user.getInGameAccountSet());
            for (InGameAccount inGameAccount : user.getInGameAccountSet()){
                Integer deaths = updateGameHistory(user, GameName.valueOf(inGameAccount.getGame().getName()));
                message.add(user.getUsername() +" "+
                         deaths.toString() + " смертей у грі " + inGameAccount.getGame().getName() +
                        " на акаунті " + inGameAccount.getInGameAccountName());

            }
        }
        return message;
    }

    public Integer updateGameHistory(User user, GameName game) throws ApiAccessException {
        InGameAccount inGameAccount = lastMatchRepo.getByUserAndGame_Name(user, game.name());

        GameDriver driver = gameDriverFactory.findStrategy(game);
        Integer deaths = driver.getDeaths(inGameAccount);


        user.getCounters().forEach((counter -> {
            Multiplier multiplier = multiplierRepo.getByUserAndGame_NameAndExercise(user, game.name(), counter.getExercise());
            counterService.addToCounter(counter, deaths * multiplier.getMultiplier());
        }
        ));
        return deaths;
    }

    public InGameAccount bindInGameAccount(User user, String inGameAccountName, GameName game) throws ApiAccessException {
        GameDriver driver = gameDriverFactory.findStrategy(game);
        return driver.bindAccount(inGameAccountName, user);
    }

    public void test() throws JsonProcessingException {
        Iterable<InGameAccount> inGameAccounts = inGameAccountRepository.findAll();
        for (InGameAccount acc : inGameAccounts) {
            acc.setMark(null);
            acc.setDay(null);
            inGameAccountRepository.save(acc);
        }
        counterRepository.findAll().forEach((c)->{
            c.setCounter((double) Math.round(Math.random() * 100));
            counterRepository.save(c);
        });
    }
}
