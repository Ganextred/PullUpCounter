package com.example.pullupcounter.data.repository;


import com.example.pullupcounter.data.entity.Exercise;
import com.example.pullupcounter.data.entity.Multiplier;
import com.example.pullupcounter.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
