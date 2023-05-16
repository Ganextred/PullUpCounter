package com.example.pullupcounter.controller;

import com.example.pullupcounter.data.entity.Exercise;
import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.data.repository.CounterRepository;
import com.example.pullupcounter.data.repository.ExerciseRepository;
import com.example.pullupcounter.data.repository.UserRepository;
import com.example.pullupcounter.model.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    CounterRepository counterRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExerciseRepository exerciseRepo;
    @Autowired
    UserService userService;

    @GetMapping("/countersRatio")
    public List<Object> apartmentRatio(Model model) {
        List<Object> ratio = new ArrayList<>();

        List<Object> meta = new ArrayList<>();
        meta.add("Користувач");
        List<Exercise> exercises = exerciseRepo.findAll();
        exercises.sort(Comparator.comparing(Exercise::getName));
        meta.addAll(exercises.stream().map(Exercise::getName).toList());
        ratio.add(meta);

        Iterable<User> users = userRepository.findAll();
        users.forEach(x -> {
            List<Object> row = new ArrayList<>();
            row.add(x.getUsername());
            exercises.forEach(e -> row.add(x.getCounterByExercise(e.getName()).getCounter()));
            ratio.add(row);
        });

        System.out.println(ratio);
        return ratio;
    }

    @GetMapping("/test")
    public String test(Model model) throws JsonProcessingException {
        userService.test();
        return "index";
    }
}
