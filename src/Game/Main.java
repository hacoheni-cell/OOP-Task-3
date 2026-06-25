package Game;

import BusinessLayer.CLI;
import Combat_System.CombatSystem;
import Combat_System.DefaultCombat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CombatSystem combatSystem = new DefaultCombat();
        CLI cli = new CLI();
        if (args.length == 0) {
            System.out.println("Error: Missing levels directory path.");
            return;
        }
        List<Level> levels = loadLevels(args[0], combatSystem, cli);
        if (levels.isEmpty()) {
            cli.displayMessage("No levels were loaded. Exiting...");
            return;
        }
        MessageCallback printer = (msg) -> cli.displayMessage(msg);
        InputCallback reader = () -> cli.getInput();
        GameController controller = new GameController(printer, reader, levels, combatSystem);
        controller.start();
        cli.close();
    }
    private static List<Level> loadLevels(String dirPath, CombatSystem combatSystem, CLI cli) {
        List<Level> levels = new ArrayList<>();
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            cli.displayMessage("Error: Invalid directory path.");
            return levels;
        }
        int levelNumber = 1;
        while (true) {
            File levelFile = new File(dir, "level" + levelNumber + ".txt");
            if (!levelFile.exists()) {
                break;
            }
            try {
                List<String> levelData = Files.readAllLines(levelFile.toPath());
                Level level = new Level(levelData, combatSystem);
                levels.add(level);
            } catch (IOException e) {
                cli.displayMessage("Error reading file: " + levelFile.getName());
            }
            levelNumber++;
        }

        return levels;
    }
}