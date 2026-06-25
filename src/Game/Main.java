package Game;

import Combat_System.CombatSystem;
import Combat_System.DefaultCombat;
import BusinessLayer.CLI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CombatSystem combatSystem = new DefaultCombat();
        CLI cli = new CLI();
        MessageCallback printer = (msg) -> cli.displayMessage(msg);
        InputCallback reader = () -> cli.getInput();
        if (args.length == 0) {
            printer.send("Error: Missing levels directory path.");
            return;
        }
        List<Level> levels = loadLevels(args[0], combatSystem, printer);
        if (levels.isEmpty()) {
            printer.send("No levels were loaded. Exiting...");
            return;
        }

        GameController controller = new GameController(printer, reader, levels, combatSystem);
        controller.start();
        cli.close();
    }
    private static List<Level> loadLevels(String dirPath, CombatSystem combatSystem, MessageCallback messageSender) {
        List<Level> levels = new ArrayList<>();
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            messageSender.send("Error: Invalid directory path.");
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
                Level level = new Level(levelData, combatSystem, messageSender );
                levels.add(level);
            } catch (IOException e) {
                messageSender.send("Error reading file: " + levelFile.getName());
            }
            levelNumber++;
        }

        return levels;
    }
}