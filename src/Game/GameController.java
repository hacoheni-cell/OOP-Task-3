package Game;

import java.util.ArrayList;
import java.util.List;

import Combat_System.CombatSystem;
import Units.*;

public class GameController {
    private MessageCallback messageSender;
    private InputCallback inputProvider;
    private List<Player> availablePlayers;
    private Player currentPlayer;
    private List<Level> levels;
    private  CombatSystem combatSystem;

    public GameController(MessageCallback messageSender, InputCallback inputProvider, List<Level> levels, CombatSystem combatSystem) {
        this.messageSender = messageSender;
        this.inputProvider = inputProvider;
        this.levels = levels;
        this.availablePlayers = new ArrayList<>();
        initializePlayers();
        this.combatSystem = combatSystem;
    }

    private void initializePlayers() {
        Position initPos = new Position(0, 0);
        String playerTile = "@";
        availablePlayers.add(new Warrior("Jon Snow", 300, 30, 4, initPos, combatSystem, playerTile, 3, this.messageSender));
        availablePlayers.add(new Warrior("The Hound", 400, 20, 6, initPos, combatSystem, playerTile, 5, this.messageSender));
        availablePlayers.add(new Mage("Melisandre", 100, 5, 1, initPos, combatSystem, 300, 30, 15, 5, 6, playerTile, this.messageSender));
        availablePlayers.add(new Mage("Thoros of Myr", 250, 25, 4, initPos, combatSystem, 150, 20, 20, 3, 4, playerTile, this.messageSender));
        availablePlayers.add(new Rogue("Arya Stark", 150, 40, 2, initPos, combatSystem, playerTile, 20, this.messageSender));
        availablePlayers.add(new Rogue("Bronn", 250, 35, 3, initPos, combatSystem, playerTile, 50, this.messageSender));
        availablePlayers.add(new Hunter("Ygritte", 220, 30, 2, initPos, combatSystem, 6, playerTile, this.messageSender));
    }
    public void start() {
        selectPlayer();
        for (Level level : levels) {
            if (currentPlayer.isDead()) {
               PlayerDied(level);
               return;
            }
            playLevel(level, this.currentPlayer);
        }

        if (currentPlayer.isDead()) {
            PlayerDied(levels.get(levels.size() - 1));
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

    private void playLevel(Level level, Player player) {
        Position startPos = level.getPlayerInitalPosition();
        currentPlayer.setPosition(startPos);
        level.setPlayer(player);
        level.setPlayerInInitPos(currentPlayer);
        while (!level.isCleared() && !currentPlayer.isDead()) {
            messageSender.send(level.getBoard().toString());
            messageSender.send(currentPlayer.Description());
            messageSender.send("Enter your move (w, a, s, d, e, q): ");
            player.GameTick();
            String input = inputProvider.getInput();
            currentPlayer.processInput(input, level);
            for (Enemy enemy : level.getEnemies()) {
                enemy.GameTick();
                if (currentPlayer.isDead()) {
                    PlayerDied(level);
                    return;
                }
                enemy.takeTurn(level);
            }
            level.removeDeadEnemies();
        }
    }
    private void PlayerDied(Level level) {
        messageSender.send(level.getBoard().toString());
        messageSender.send("You have been defeated! Game Over.");

    }
}