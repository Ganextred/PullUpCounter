package com.example.pullupcounter.data.entity;

import com.example.pullupcounter.model.enums.GameName;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<InGameAccount> inGameAccountSet;

    public List<InGameAccount> getInGameAccountSet() {
        return inGameAccountSet;
    }

    public void setInGameAccountSet(List<InGameAccount> inGameAccountSet) {
        this.inGameAccountSet = inGameAccountSet;
    }

    public List<Multiplier> getMultipliers() {
        return multipliers;
    }

    public void setMultipliers(List<Multiplier> multipliers) {
        this.multipliers = multipliers;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Counter> counters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Multiplier> multipliers;

    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private String username;

    public List<InGameAccount> getLastMatchSet() {
        return inGameAccountSet;
    }

    public void setLastMatchSet(List<InGameAccount> inGameAccountSet) {
        this.inGameAccountSet = inGameAccountSet;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
    }

    public boolean hasInGameAccount(String gameName) {
        return inGameAccountSet.stream().anyMatch(x -> x.getGame().getName().equals(gameName));
    }

    public Counter getCounterByExercise(String exercise) {
        return counters.stream().filter(counter -> exercise.equals(counter.getExercise().getName())).findAny().get();
    }
}
