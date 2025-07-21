package br.edu.ifmg.poo.game.word;

public class Word {
    private final String original;
    private final char[] letters;
    private final boolean[] revealed;

    public Word(String original) {
        if (!isValidWord(original)) {
            throw new IllegalArgumentException("Palavra inválida!");
        }
        this.original = original.toLowerCase();
        this.letters = this.original.toCharArray();
        this.revealed = new boolean[letters.length];
    }

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

    public String mask() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letters.length; i++) {
            sb.append(revealed[i] ? letters[i] : '_').append(' ');
        }
        return sb.toString();
    }

    public boolean isFullyRevealed() {
        for (boolean r : revealed) {
            if (!r) return false;
        }
        return true;
    }

    public String getOriginal() {
        return original;
    }

    public static boolean isValidWord(String w) {
        return w.chars().allMatch(Character::isLetter);
    }
}