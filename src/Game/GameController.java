package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Combat_System.CombatSystem;
import Combat_System.DefaultCombat;
import Units.Enemy;
import Units.Mage;
import Units.Player;
import Units.Warrior;

public class GameController {
    private MessageCallback messageSender;
    private InputCallback inputProvider;
    private List<Player> availablePlayers;
    private Player currentPlayer;
    private List<Level> levels;

    public GameController(MessageCallback messageSender, InputCallback inputProvider, List<Level> levels) {
        this.messageSender = messageSender;
        this.inputProvider = inputProvider;
        this.levels = levels;
        this.availablePlayers = new ArrayList<>();
        initializePlayers();
    }
    private void initializePlayers() {
        availablePlayers.add(new Warrior("Jon Snow", 300, 30, 4, 3));
        availablePlayers.add(new Warrior("The Hound", 400, 20, 6, 5));
        availablePlayers.add(new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6));
        availablePlayers.add(new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4));
        availablePlayers.add(new Rogue("Arya Stark", 150, 40, 2, 20));
        availablePlayers.add(new Rogue("Bronn", 250, 35, 3, 50));
    }
    public void start() {
        selectPlayer();
        for (Level level : levels) {
            if (currentPlayer.isDead()) {
               PlayerDied();
               return;
            }
            playLevel(level);
        }

        if (currentPlayer.isDead()) {
            PlayerDied();
            return;
        }
        else {
            messageSender.send("You won!");
        }
    }
    private void selectPlayer() {
        messageSender.send("Select player:");
        for (int i = 0; i < availablePlayers.size(); i++) {
            messageSender.send((i + 1) + ". " + availablePlayers.get(i).Description());
        }

        boolean validChoice = false;
        while (!validChoice) {
            String input = inputProvider.getInput();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= availablePlayers.size()) {
                    currentPlayer = availablePlayers.get(choice - 1);
                    messageSender.send("You have selected: " + currentPlayer.getName());
                    validChoice = true;
                } else {
                    messageSender.send("Invalid choice. Please select a number between 1 and " + availablePlayers.size());
                }
            } catch (NumberFormatException e) {
                messageSender.send("Invalid input. Please enter a number.");
            }
        }
    }

    private void playLevel(Level level) {
        Position startPos = level.getPlayerInitalPosition();
        currentPlayer.setPosition(startPos);
        level.setPlayerInInitPos(currentPlayer);
        while (!level.isCleared() && !currentPlayer.isDead()) {
            messageSender.send(level.getBoard().toString());
            messageSender.send(currentPlayer.Description());
            messageSender.send("Enter your move (w, a, s, d, e, q): ");
            String input = inputProvider.getInput();
            currentPlayer.processInput(input);
            for (Enemy enemy : level.getEnemies()) {
                if (currentPlayer.isDead()) {
                    PlayerDied();
                    return;
                }
                enemy.takeTurn(currentPlayer);
            }
            level.removeDeadEnemies();
        }
    }
    private void PlayerDied() {
        messageSender.send("You have been defeated! Game Over.");
        if (currentPlayer != null) {
            currentPlayer.setName("X");
        }
    }
}