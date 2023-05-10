package com.example.pullupcounter.model.service;

import com.example.pullupcounter.model.enums.GameName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class GameDriverFactory {
    private Map<GameName, GameDriver> drivers;

    @Autowired
    public GameDriverFactory(Set<GameDriver> strategySet) {
        createDrivers(strategySet);
    }

    public GameDriver findStrategy(GameName strategyName) {
        return drivers.get(strategyName);
    }

    private void createDrivers(Set<GameDriver> strategySet) {
        drivers = new HashMap<GameName, GameDriver>();
        strategySet.forEach(
                driver -> drivers.put(driver.getName(), driver));
    }
}
