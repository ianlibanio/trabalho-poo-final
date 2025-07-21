package br.edu.ifmg.poo.game.player;

import java.util.List;
import java.util.Scanner;

// Jogador humano, recebe entrada pelo teclado (usado apenas em modo console)
public class HumanPlayer extends Player {

    private final Scanner scanner;

    public HumanPlayer(String name, Scanner scanner) {
        super(name);
        this.scanner = scanner;
    }

    // Solicita ao usuário uma letra que ainda não foi tentada
    @Override
    public char nextGuess(List<Character> alreadyTried) {
        char letter = 0;
        do {
            System.out.print("Digite uma letra: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Entrada inválida! Digite apenas uma letra.");
                continue;
            }

            letter = input.charAt(0);
            if (alreadyTried.contains(letter)) {
                System.out.println("Letra repetida! Tente outra.");
                letter = 0;
            }
        } while (letter == 0);
        return letter;
    }
}