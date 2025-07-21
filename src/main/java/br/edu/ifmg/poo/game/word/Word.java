package br.edu.ifmg.poo.game.word;

public class Word {
    private final String original; // Palavra original
    private final char[] letters; // Letras da palavra
    private final boolean[] revealed; // Quais letras já foram reveladas

    public Word(String original) {
        if (!isValidWord(original)) {
            throw new IllegalArgumentException("Palavra inválida!");
        }
        this.original = original.toLowerCase();
        this.letters = this.original.toCharArray();
        this.revealed = new boolean[letters.length];
    }

    // Verifica se a palavra é válida (apenas letras)
    public static boolean isValidWord(String w) {
        return w.chars().allMatch(Character::isLetter);
    }

    // Revela todas as ocorrências da letra na palavra
    public boolean reveal(char letter) {
        boolean hit = false;
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == letter) {
                revealed[i] = true;
                hit = true;
            }
        }
        return hit;
    }

    // Retorna a palavra mascarada (_ para letras não reveladas)
    public String mask() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letters.length; i++) {
            sb.append(revealed[i] ? letters[i] : '_').append(' ');
        }
        return sb.toString();
    }

    // Verifica se todas as letras foram reveladas
    public boolean isFullyRevealed() {
        for (boolean r : revealed) {
            if (!r) return false;
        }
        return true;
    }

    // Retorna a palavra original
    public String getOriginal() {
        return original;
    }
}