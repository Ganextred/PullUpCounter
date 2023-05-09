package com.example.pullupcounter.data.entity;

import javax.persistence.*;

@Entity
public class Multiplier {
    @EmbeddedId
    private MultiplierCompoundId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("gameId")
    private Game game;

    @ManyToOne
    @MapsId("exerciseId")
    private Exercise exercise;

    public MultiplierCompoundId getId() {
        return id;
    }

    public void setId(MultiplierCompoundId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    private double multiplier;
}
