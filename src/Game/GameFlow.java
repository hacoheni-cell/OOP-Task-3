package Game;

import Units.Player;
import Units.Unit;
import Game.Callbacks.MessageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameFlow {
    private Player player;
    private Level currentLevel;
    private Scanner scanner;
    private MessageCallback msgCallback;
    private

    public GameFlow(Player selectedPlayer, MessageCallback msgCallback) {
        this.player = selectedPlayer;
        this.scanner = new Scanner(System.in);
        this.msgCallback = msgCallback;
    }

    public void startLevel(Level level) {
        this.currentLevel = level;
        msgCallback.send("Starting new level...");

        while (!player.isDead() && !currentLevel.isCleared()) {
            msgCallback.send(currentLevel.getBoard().toString());
            msgCallback.send(player.description());

            char input = getUserInput();

            player.performAction(input);
            player.processStep(currentLevel);

            List<Unit> aliveEnemies = currentLevel.getEnemies();
            for (Unit enemy : aliveEnemies) {
                if (!enemy.isDead()) {
                    enemy.processStep(currentLevel);
                }
            }

            currentLevel.removeDeadEnemies();
        }

        if (player.isDead()) {
            msgCallback.send("Game Over.");
        } else {
            msgCallback.send("Level Cleared!");
        }
    }

    private char getUserInput() {
        String input = scanner.nextLine();
        if (input.length() > 0) {
            return input.charAt(0);
        }
        return ' ';
    }

    public boolean performAction(char input) {
        Runnable action = inputActions.get(input);
        if (action != null) {
            action.run();
            return true;
        }
        return false;
    }
    private Map<Character, Runnable> inputActions = new HashMap<>();

    private void initializeInputActions() {

        inputActions.put('w', () -> this.currentLevel.getCell(player.getPos(), 0, 1).Accept(player));
        inputActions.put('s', () -> this.currentLevel.getCell(player.getPos(), 0, -1).Accept(player));
        inputActions.put('a', () -> this.currentLevel.getCell(player.getPos(), -1, 0).Accept(player));
        inputActions.put('d', () -> this.currentLevel.getCell(player.getPos(), 1, 0).Accept(player));
        inputActions.put('e', () -> player.cast(this.currentLevel.getUnitsInRange(player.getPos(), player.GetRange())));
        inputActions.put('q', () -> {});
    }

}