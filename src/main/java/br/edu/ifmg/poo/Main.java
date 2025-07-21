package br.edu.ifmg.poo;

import br.edu.ifmg.poo.game.HangmanGame;
import br.edu.ifmg.poo.game.player.HumanPlayer;
import br.edu.ifmg.poo.game.player.Player;
import br.edu.ifmg.poo.game.player.RobotPlayer;
import br.edu.ifmg.poo.game.word.Word;
import br.edu.ifmg.poo.util.HangmanDrawer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.*;

public class Main extends Application {
    private Player lastToReveal;
    private Stage primaryStage;

    private HangmanGame game;
    private int currentChooserIndex = 0, currentGuesserIndex;

    private List<Player> guessers;

    private final List<Player> players = new ArrayList<>();
    private final Map<Player, Integer> scoreboard = new HashMap<>();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        this.showMenu();
    }

    private void showMenu() {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        final Label title = new Label("Jogo da Forca");

        final Button btnAddHuman = new Button("Cadastrar jogador HUMANO");
        final Button btnAddRobot = new Button("Cadastrar jogador ROBÔ");
        final Button btnStart = new Button("Iniciar partida");
        final Button btnScore = new Button("Ver placar");
        final Button btnExit = new Button("Sair");

        btnAddHuman.setOnAction(e -> showAddHuman());
        btnAddRobot.setOnAction(e -> showAddRobot());
        btnStart.setOnAction(e -> startMatch());
        btnScore.setOnAction(e -> showScoreboard());
        btnExit.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(title, btnAddHuman, btnAddRobot, btnStart, btnScore, btnExit);

        final Scene scene = new Scene(root, 350, 300);
        scene.getStylesheets().add(getClass().getResource("/hangman.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Jogo da Forca");
        primaryStage.show();
    }

    private void showAddHuman() {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label label = new Label("Nome do jogador humano:");
        final TextField nameField = new TextField();
        final Button btnAdd = new Button("Cadastrar");
        final Label msg = new Label();

        btnAdd.setOnAction(e -> {
            final String name = nameField.getText().trim();
            if (name.isEmpty()) {
                msg.setText("Nome não pode ser vazio!");
                return;
            }

            final Player player = new HumanPlayer(name, null);

            scoreboard.put(player, 0);
            players.add(player);

            msg.setText("Jogador humano " + name + " cadastrado!");
            nameField.clear();
        });

        final Button btnBack = new Button("Voltar");
        btnBack.setOnAction(e -> showMenu());
        root.getChildren().addAll(label, nameField, btnAdd, msg, btnBack);

        final Scene scene = new Scene(root, 350, 250);
        scene.getStylesheets().add(getClass().getResource("/hangman.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showAddRobot() {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label label = new Label("Nome do robô (deixe vazio para padrão):");
        final TextField nameField = new TextField();
        final Button btnAdd = new Button("Cadastrar");
        final Label msg = new Label();

        btnAdd.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Bot" + (players.size() + 1);

            final Player player = new RobotPlayer(name);

            scoreboard.put(player, 0);
            players.add(player);

            msg.setText("Jogador robô " + name + " cadastrado!");
            nameField.clear();
        });

        final Button btnBack = new Button("Voltar");
        btnBack.setOnAction(e -> showMenu());
        root.getChildren().addAll(label, nameField, btnAdd, msg, btnBack);

        final Scene scene = new Scene(root, 350, 250);
        scene.getStylesheets().add(getClass().getResource("/hangman.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void startMatch() {
        if (players.size() < 2) {
            showAlert("Cadastre pelo menos 2 jogadores para jogar!");
            return;
        }

        if (players.stream().noneMatch(p -> p instanceof HumanPlayer)) {
            showAlert("Cadastre pelo menos um jogador humano para jogar!");
            return;
        }

        this.currentChooserIndex = 0;
        this.scoreboard.clear();

        for (Player p : players) {
            this.scoreboard.put(p, 0);
        }

        this.startRound();
    }

    private void startRound() {
        if (currentChooserIndex >= players.size()) {
            this.showScoreboard();
            return;
        }

        final Player chooser = players.get(currentChooserIndex);
        final List<Player> guessersList = new ArrayList<>(players);

        guessersList.remove(chooser);

        this.guessers = guessersList;
        this.currentGuesserIndex = 0;
        this.lastToReveal = null;

        if (chooser instanceof RobotPlayer robot) {
            final String word = robot.chooseWord();
            this.showAlert("(ROBÔ) Palavra secreta escolhida. Boa sorte!");

            this.game = new HangmanGame(new Word(word));
            this.showGameScreen(word, chooser, true);
        } else {
            this.showWordInputScreen(chooser);
        }
    }

    private void showWordInputScreen(Player chooser) {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label label = new Label("(" + chooser.getName() + ") Digite a palavra sem acentos:");
        final PasswordField wordField = new PasswordField();
        final Button btnOk = new Button("OK");
        final Label msg = new Label();

        btnOk.setOnAction(e -> {
            String word = wordField.getText().trim().toLowerCase();

            if (!Word.isValidWord(word)) {
                msg.setText("Palavra inválida! Tente novamente.");
                return;
            }

            this.game = new HangmanGame(new Word(word));
            this.showGameScreen(word, chooser);
        });

        root.getChildren().addAll(label, wordField, btnOk, msg);
        primaryStage.setScene(new Scene(root, 400, 180));
    }

    private void showGameScreen(String word, Player chooser) {
        this.showGameScreen(word, chooser, false);
    }

    private void showGameScreen(String word, Player chooser, boolean autoPlayRobot) {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label info = new Label("Palavra escolhida! Jogadores vão tentar adivinhar.");
        final Label wordLabel = new Label("Palavra: " + game.getMaskedWord());
        final Label attemptsLabel = new Label("Tentativas restantes: " + game.getRemainingAttempts());
        final Label triedLabel = new Label("Letras tentadas: ");
        final Label vezLabel = new Label();
        final TextField guessField = new TextField();
        final Button btnGuess = new Button("Chutar");
        final Label msg = new Label();

        final Label hangmanLabel = new Label(HangmanDrawer.draw(HangmanGame.MAX_ATTEMPTS - game.getRemainingAttempts()));
        hangmanLabel.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 16px; -fx-text-fill: #FFD700;");
        hangmanLabel.setMinHeight(140); // Garante espaço para o desenho completo
        hangmanLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
        hangmanLabel.setWrapText(false); // Não quebra linhas automaticamente
        hangmanLabel.setMaxWidth(Double.MAX_VALUE);

        // Garante que as quebras de linha sejam respeitadas
        hangmanLabel.setStyle(hangmanLabel.getStyle() + "; -fx-alignment: top-left;");

        // Adiciona o desenho da forca de forma segura
        if (!root.getChildren().isEmpty()) {
            root.getChildren().add(1, hangmanLabel);
        } else {
            root.getChildren().add(hangmanLabel);
        }

        final Runnable updateUI = () -> {
            wordLabel.setText("Palavra: " + game.getMaskedWord());
            attemptsLabel.setText("Tentativas restantes: " + game.getRemainingAttempts());
            triedLabel.setText("Letras tentadas: " + game.getTriedLetters());
            vezLabel.setText("Vez de: " + nextGuesserName());
            // Atualiza o desenho do hangman conforme os erros
            int errors = HangmanGame.MAX_ATTEMPTS - game.getRemainingAttempts();
            hangmanLabel.setText(HangmanDrawer.draw(errors));
        };

        btnGuess.setOnAction(e -> {
            Player currentGuesser = guessers.get(currentGuesserIndex);
            if (currentGuesser instanceof RobotPlayer) {
                // Nunca permita clique manual quando for robô
                return;
            }

            this.handleGuess(word, chooser, guessField, btnGuess, msg, updateUI);
        });

        vezLabel.setText("Vez de: " + nextGuesserName());
        root.getChildren().addAll(info, wordLabel, attemptsLabel, triedLabel, vezLabel, guessField, btnGuess, msg);

        final Scene scene = new Scene(root, 500, 450);
        scene.getStylesheets().add(getClass().getResource("/hangman.css").toExternalForm());
        primaryStage.setScene(scene);

        // Sempre que for a vez do robô, desabilite o botão e inicie jogada automática
        if (guessers.get(currentGuesserIndex) instanceof RobotPlayer) {
            btnGuess.setDisable(true);
            this.autoRobotPlay(word, chooser, guessField, btnGuess, msg, updateUI);
        } else {
            btnGuess.setDisable(false);
        }
    }

    private String nextGuesserName() {
        return guessers.get(currentGuesserIndex).getName();
    }

    private void showEndOfRound(String word, Player chooser) {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label endMsg;
        if (game.getMaskedWord().replace(" ", "").equals(word) && lastToReveal != null) {
            endMsg = new Label("Parabéns, " + lastToReveal.getName() + ", você adivinhou a última letra e ganhou 1 ponto!");
            scoreboard.put(lastToReveal, scoreboard.getOrDefault(lastToReveal, 0) + 1);
        } else {
            endMsg = new Label("Fim das tentativas! A palavra era '" + word + "'.");
        }

        final Button btnNext = new Button("Próxima rodada");
        btnNext.setOnAction(e -> {
            currentChooserIndex++;
            startRound();
        });

        root.getChildren().addAll(endMsg, btnNext);
        primaryStage.setScene(new Scene(root, 400, 120));
    }

    private void showScoreboard() {
        final VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        final Label title = new Label("=========== PLACAR ===========");
        final List<Player> sorted = new ArrayList<>(scoreboard.keySet());

        sorted.sort((a, b) -> scoreboard.get(b) - scoreboard.get(a));

        final VBox placarBox = new VBox(5);
        for (Player p : sorted) {
            placarBox.getChildren().add(new Label(p.getName() + " : " + scoreboard.get(p) + " ponto(s)"));
        }

        final Button btnBack = new Button("Voltar ao menu");

        btnBack.setOnAction(e -> showMenu());
        root.getChildren().addAll(title, placarBox, btnBack);
        primaryStage.setScene(new Scene(root, 350, 200));
    }

    private void showAlert(String msg) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void handleGuess(String word, Player chooser, TextField guessField, Button btnGuess, Label msg, Runnable updateUI) {
        Player currentGuesser = guessers.get(currentGuesserIndex);
        String guess;
        if (currentGuesser instanceof RobotPlayer) {
            btnGuess.setDisable(true);
            guess = String.valueOf(((RobotPlayer) currentGuesser).nextGuess(new ArrayList<>(game.getTriedLetters())));
            msg.setText(currentGuesser.getName() + " (Robô) chutou: " + guess);
        } else {
            btnGuess.setDisable(false);
            guess = guessField.getText().trim().toLowerCase();
            if (guess.isEmpty()) {
                msg.setText("Digite uma letra ou palavra!");
                return;
            }
        }
        String result = game.tryGuess(guess);
        msg.setText(currentGuesser.getName() + ": " + result);
        guessField.clear();
        updateUI.run();
        if (game.isGameOver()) {
            btnGuess.setDisable(true);
            if (game.getMaskedWord().replace(" ", "").equals(word)) {
                lastToReveal = currentGuesser;
            }
            showEndOfRound(word, chooser);
        } else {
            currentGuesserIndex = (currentGuesserIndex + 1) % guessers.size();
            updateUI.run();
            if (guessers.get(currentGuesserIndex) instanceof RobotPlayer) {
                btnGuess.setDisable(true);
                autoRobotPlay(word, chooser, guessField, btnGuess, msg, updateUI);
            } else {
                btnGuess.setDisable(false);
            }
        }
    }

    private void autoRobotPlay(String word, Player chooser, TextField guessField, Button btnGuess, Label msg, Runnable updateUI) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
        pause.setOnFinished(ev -> handleGuess(word, chooser, guessField, btnGuess, msg, updateUI));
        pause.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
