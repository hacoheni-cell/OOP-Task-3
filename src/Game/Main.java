package Game;

import Combat_System.CombatSystem;
import Combat_System.DefaultCombat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CombatSystem combatSystem = new DefaultCombat();
        if (args.length == 0) {
            System.out.println("Error: Missing levels directory path.");
            return;
        }
        List<Level> levels = loadLevels(args[0], combatSystem);
        if (levels.isEmpty()) {
            System.out.println("No levels were loaded. Exiting...");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        MessageCallback printer = (msg) -> System.out.println(msg);
        InputCallback reader = () -> scanner.nextLine();
        GameController controller = new GameController(printer, reader, levels, combatSystem);
        controller.start();
        scanner.close();
    }
    private static List<Level> loadLevels(String dirPath, CombatSystem combatSystem) {
        List<Level> levels = new ArrayList<>();
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Error: Invalid directory path.");
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
                System.out.println("Error reading file: " + levelFile.getName());
            }
            levelNumber++;
        }

        return levels;
    }
}