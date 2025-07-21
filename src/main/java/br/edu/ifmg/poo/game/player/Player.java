package br.edu.ifmg.poo.game.player;

import java.util.List;

public abstract class Player {

    private final String name;
    private int points;

    protected Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoint() {
        points++;
    }

    public abstract char nextGuess(List<Character> alreadyTried);
}