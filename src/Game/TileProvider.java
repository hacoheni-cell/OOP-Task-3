package Game;

import Units.Enemy;
import Units.Monster;
import Units.Trap;
import Units.Boss;
import Combat_System.CombatSystem;

public class TileProvider {

    public static Enemy createEnemy(char c, Position pos, CombatSystem combatSystem, MessageCallback messageSender) {
        String tileString = String.valueOf(c);

        switch (tileString) {
            case "s":
                return new Monster(3, 25, "Gold Cloak", 80, 80, 8, 3, pos, tileString, combatSystem, messageSender);
            case "k":
                return new Monster(4, 50, "Lannister Knight", 200, 200, 14, 8, pos, tileString, combatSystem, messageSender);
            case "q":
                return new Monster(5, 100, "Queen's Guard", 400, 400, 20, 15, pos, tileString, combatSystem, messageSender);
            case "z":
                return new Monster(3, 100, "Wright", 600, 600, 30, 15, pos, tileString, combatSystem, messageSender);
            case "b":
                return new Monster(4, 250, "Bear", 1000, 1000, 75, 30, pos, tileString, combatSystem, messageSender);
            case "g":
                return new Monster(5, 500, "Giant", 1500, 1500, 100, 40, pos, tileString, combatSystem, messageSender);
            case "w":
                return new Monster(6, 1000, "White Walker", 2000, 2000, 150, 50, pos, tileString, combatSystem, messageSender);

            // --- Bosses ---
            case "M":
                return new Boss(6, 500, "The Mountain", 1000, 1000, 60, 25, pos, tileString, combatSystem, 5, messageSender);
            case "C":
                return new Boss(1, 1000, "Queen Cersei", 100, 100, 10, 10, pos, tileString, combatSystem, 8, messageSender);
            case "K":
                return new Boss(8, 5000, "Night's King", 5000, 5000, 300, 150, pos, tileString, combatSystem, 3, messageSender);

            // --- Traps ---
            case "B":
                return new Trap(1, 5, 0, true, 250, "Bonus Trap", 1, 1, 1, 1, pos, 1, tileString, combatSystem, messageSender);
            case "Q":
                return new Trap(3, 7, 0, true, 100, "Queen's Trap", 250, 250, 50, 10, pos, 1, tileString, combatSystem, messageSender);
            case "D":
                return new Trap(1, 10, 0, true, 250, "Death Trap", 500, 500, 100, 20, pos, 1, tileString, combatSystem, messageSender);

            default:
                return null;
        }
    }
}