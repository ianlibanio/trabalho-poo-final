package br.edu.ifmg.poo.game;

import br.edu.ifmg.poo.game.word.Word;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HangmanGame {
    public static final int MAX_ATTEMPTS = 6;
    private final Word word;
    private final Set<Character> triedLetters;
    private int remainingAttempts;
    private boolean gameOver;

    public HangmanGame(Word word) {
        this.word = word;
        this.remainingAttempts = MAX_ATTEMPTS;
        this.triedLetters = new HashSet<>();
        this.gameOver = false;
    }

    public String tryGuess(String guess) {
        if (gameOver) return "O jogo acabou!";
        guess = guess.toLowerCase();
        if (guess.length() == 1) {
            char letter = guess.charAt(0);
            if (!Character.isLetter(letter)) return "Digite uma letra válida!";
            if (triedLetters.contains(letter)) return "Letra já tentada!";
            triedLetters.add(letter);
            if (word.reveal(letter)) {
                if (word.isFullyRevealed()) {
                    gameOver = true;
                    return "Parabéns! Você acertou a palavra!";
                }
                return "Acertou!";
            } else {
                remainingAttempts--;
                if (remainingAttempts <= 0) {
                    gameOver = true;
                    return "Errou! Fim das tentativas.";
                }
                return "Errou!";
            }
        } else {
            if (guess.equals(word.getOriginal())) {
                for (char c : guess.toCharArray()) word.reveal(c);
                gameOver = true;
                return "Parabéns! Você acertou a palavra!";
            } else {
                remainingAttempts--;
                if (remainingAttempts <= 0) {
                    gameOver = true;
                    return "Errou! Fim das tentativas.";
                }
                return "Palavra incorreta!";
            }
        }
    }

    public String getMaskedWord() {
        return word.mask();
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Set<Character> getTriedLetters() {
        return Collections.unmodifiableSet(triedLetters);
    }
}