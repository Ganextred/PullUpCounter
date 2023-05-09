package com.example.pullupcounter.data.entity;

import javax.persistence.*;

@Entity
public class LastMatch {
    @Id
    @Column(name = "last_match_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Game game;

    private String mark;
}
