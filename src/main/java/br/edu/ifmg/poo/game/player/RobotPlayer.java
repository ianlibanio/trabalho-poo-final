package br.edu.ifmg.poo.game.player;

import java.util.List;
import java.util.Random;

public class RobotPlayer extends Player {

    private static final Random RAND = new Random();
    private static final List<String> DICTIONARY = List.of(
            "java", "forca", "engenharia", "computador", "poo", "terminal", "ifmg", "bambui", "codigo", "algoritmo");

    public RobotPlayer(String name) {
        super(name);
    }

    public String chooseWord() {
        int randomIndex = RAND.nextInt(DICTIONARY.size());

        return DICTIONARY.get(randomIndex);
    }

    @Override
    public char nextGuess(List<Character> alreadyTried) {
        char letter;
        do {
            letter = (char) ('a' + RAND.nextInt(26));
        } while (alreadyTried.contains(letter));
        System.out.println(getName() + " (Robô) chuta a letra: " + letter);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        return letter;
    }
}