package br.edu.ifmg.poo.util;

// Classe utilitária para desenhar o estado da forca em texto
public class HangmanDrawer {

    // Array com os desenhos para cada quantidade de erros
    private static final String[] STATES = {
        "_______\n|     |\n|\n|\n|\n|",
        "_______\n|     |\n|   (o-o)\n|\n|\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|     |\n|",
        "_______\n|     |\n|   (o-o)\n|    /|\\\n|     |\n|    /",
        "_______\n|     |\n|   (x-x)\n|    /|\\\n|     |\n|    / \\"
    };

    // Retorna o desenho correspondente ao número de erros
    public static String draw(int errors) {
        return STATES[Math.min(errors, STATES.length - 1)];
    }
}