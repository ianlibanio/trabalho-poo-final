package br.edu.ifmg.poo.util;

public class HangmanDrawer {

    private static final String[] STATES = {
        "_______\n|     |\n|\n|\n|\n|",
        "_______\n|     |\n|   (o-o)\n|\n|\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|     |\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|     |\n|    /",
        "_______\n|     |\n|   (x-x)\n|    /|\\\n|     |\n|    / \\"
    };

    public static String draw(int errors) {
        return STATES[Math.min(errors, STATES.length - 1)];
    }

}