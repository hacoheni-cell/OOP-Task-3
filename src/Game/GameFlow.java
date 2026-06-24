package Game;

import java.util.List;
import java.util.Scanner;
import Units.Player;
import Units.Unit;

public class GameFlow {
    private Player player;
    private List<Level> levels;
    private Scanner scanner;

    public GameFlow(Player player, List<Level> levels) {
        this.player = player;
        this.levels = levels;
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        for (Level level : levels) {
            if (player.isDead()) {
                break;
            }
            playLevel(level);
        }

        if (player.isDead()) {
            System.out.println("Game Over.");
        } else {
            System.out.println("You won!");
        }

        scanner.close();
    }

    private void playLevel(Level level) {
        while (!level.isCleared() && !player.isDead()) {
            System.out.println(level.getBoard().toString());
            System.out.println(player.describe());

            String input = scanner.nextLine();
            player.processInput(input, level);

            for (Unit enemy : level.getEnemies()) {
                if (player.isDead()) {
                    break;
                }
                enemy.takeTurn(level);
            }

            level.removeDeadEnemies();
        }
    }
}