package br.edu.ifmg.poo.game.player;

import java.util.List;
import java.util.Random;

// Jogador robô, faz escolhas automáticas de palavra e letra
public class RobotPlayer extends Player {

    private static final Random RAND = new Random();
    // Dicionário de palavras possíveis para o robô escolher
    private static final List<String> DICTIONARY = List.of(
            "java", "forca", "engenharia", "computador", "poo", "terminal", "ifmg", "bambui", "codigo", "algoritmo");

    public RobotPlayer(String name) {
        super(name);
    }

    // Escolhe uma palavra aleatória do dicionário
    public String chooseWord() {
        int randomIndex = RAND.nextInt(DICTIONARY.size());

        return DICTIONARY.get(randomIndex);
    }

    // Chuta uma letra aleatória que ainda não foi tentada
    @Override
    public char nextGuess(List<Character> alreadyTried) {
        char letter;
        do {
            letter = (char) ('a' + RAND.nextInt(26));
        } while (alreadyTried.contains(letter));
        System.out.println(getName() + " (Robô) chuta a letra: " + letter);

        try {
            Thread.sleep(1000); // Simula tempo de "pensar"
        } catch (InterruptedException ignored) {
        }

        return letter;
    }
}