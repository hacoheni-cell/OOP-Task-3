package Units;

import Combat_System.CombatSystem;
import Game.Position;
import java.util.List;

public class Hunter extends Player {
    protected int range;
    protected int arrowsCount;
    protected int ticksCount;

    public Hunter(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, int range, char tile) {
        super(name, healthPool, attack, defence, pos,tile, combat);
        this.range = range;
        this.arrowsCount = 10 * playerLevel;
        this.ticksCount = 0;
    }

    @Override
    public void LevelUp() {
        super.LevelUp();
        this.arrowsCount += 10 * playerLevel;
        SetAttackPoints(attackPoints + (2 * playerLevel));
        SetDefencePoints(defencePoints + playerLevel);
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        if (arrowsCount == 0) {
            throw new IllegalArgumentException("No arrows left for cast.");
        }
        if (listOfUnits == null || listOfUnits.size() == 0) {
            return 0;
        }

        Unit closest = null;
        double minDistance = Double.MAX_VALUE;
        for (Unit u : listOfUnits) {
            double dist = this.position.range(u.getPos());
            if (dist < minDistance) {
                minDistance = dist;
                closest = u;
            }
        }

        if (closest != null) {
            arrowsCount--;
            this.Attack(closest);
        }
        return 1;
    }

    @Override
    public boolean Cast(Enemy enemy) {
        return this.combatUtiles.Attack(enemy, attackPoints, "Hunter");
    }

    @Override
    public void GameTick() {
        if (ticksCount == 10) {
            arrowsCount += playerLevel;
            ticksCount = 0;
        } else {
            ticksCount++;
        }
    }

    @Override
    public String Description() {
        return String.format("%s\t\tArrows: %d\t\tTicks: %d/10",
                super.Description(), this.arrowsCount, this.ticksCount);
    }

    @Override
    public int GetRange() {
        return range;
    }
}