package br.edu.ifmg.poo.game.player;

import java.util.List;

// Classe abstrata para jogadores (humano ou robô)
public abstract class Player {

    private final String name; // Nome do jogador
    private int points; // Pontuação do jogador

    protected Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    // Adiciona um ponto ao jogador
    public void addPoint() {
        points++;
    }

    // Método abstrato para chute de letra (implementado em subclasses)
    public abstract char nextGuess(List<Character> alreadyTried);
}