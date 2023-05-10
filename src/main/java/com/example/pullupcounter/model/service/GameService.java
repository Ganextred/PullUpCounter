package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.Game;
import com.example.pullupcounter.data.repository.GameRepository;
import com.example.pullupcounter.model.enums.GameName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {


    @Autowired
    GameRepository gameRepo;

    @Autowired
    GameDriverFactory gameDriverFactory;

    public Boolean updateKey(String gameName, String key) {
        GameDriver driver = gameDriverFactory.findStrategy(GameName.valueOf(gameName));
        if (!driver.validateKey(key))
            return false;
        Game game = gameRepo.getGameByName(gameName);
        game.setKey(key);
        gameRepo.save(game);
        return true;
    }

}
