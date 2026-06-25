package Game;

import Units.Enemy;
import Units.Monster;
import Units.Trap;
import Units.Boss;
import Combat_System.CombatSystem;

public class TileProvider {

    public static Enemy createEnemy(char c, Position pos, CombatSystem combatSystem) {
        String tileString = String.valueOf(c);

        switch (c) {
            case 's':
                return new Monster(3, 25, "Gold Cloak", 80, 80, 8, 3, pos, 's', combatSystem);
            case 'k':
                return new Monster(4, 50, "Lannister Knight", 200, 200, 14, 8, pos, 'k', combatSystem);
            case 'q':
                return new Monster(5, 100, "Queen's Guard", 400, 400, 20, 15, pos, 'q', combatSystem);
            case 'z':
                return new Monster(3, 100, "Wright", 600, 600, 30, 15, pos, 'z', combatSystem);
            case 'b':
                return new Monster(4, 250, "Bear", 1000, 1000, 75, 30, pos, 'b', combatSystem);
            case 'g':
                return new Monster(5, 500, "Giant", 1500, 1500, 100, 40, pos, 'g', combatSystem);
            case 'w':
                return new Monster(6, 1000, "White Walker", 2000, 2000, 150, 50, pos, 'w', combatSystem);
            case 'M':
                return new Boss(6, 500, "The Mountain", 1000, 1000, 60, 25, pos, 'M', combatSystem, 5, );
            case 'C':
                return new Boss(1, 1000, "Queen Cersei", 100, 100, 10, 10, pos, 'C', combatSystem, 8, );
            case 'K':
                return new Boss(8, 5000, "Night's King", 5000, 5000, 300, 150, pos, 'K', combatSystem, 3, );
            case 'B':
                return new Trap(1, 5, 0, true, 250, "Bonus Trap", 1, 1, 1, 1, pos, 1, 'B', combatSystem);
            case 'Q':
                return new Trap(3, 7, 0, true, 100, "Queen's Trap", 250, 250, 50, 10, pos, 1, 'Q', combatSystem);
            case 'D':
                return new Trap(1, 10, 0, true, 250, "Death Trap", 500, 500, 100, 20, pos, 1, 'D', combatSystem);
            default:
                return null;
        }
    }
}