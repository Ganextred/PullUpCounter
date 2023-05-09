package com.example.pullupcounter.data.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable

public class MultiplierCompoundId implements Serializable {
    private Long userId;
    private Long gameId;
    private Long exerciseId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
