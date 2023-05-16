package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.Counter;

import com.example.pullupcounter.data.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterService {


    @Autowired
    CounterRepository counterRepo;

    @Autowired
    GameDriverFactory gameDriverFactory;

    public Double addToCounter(Counter counter, Double number) {
        counter.setCounter(counter.getCounter() + number);
        counterRepo.save(counter);
        return counter.getCounter();
    }

}
