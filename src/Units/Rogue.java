package Units;
import Combat_System.CombatSystem;
import Game.Position;
import java.util.List;

public class Rogue extends Player {
    protected final Integer attackRange = 2;
    protected Integer cost;
    protected Integer currentEnergy;
    public Rogue(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, String tileString, Integer cost) {
        super(name, healthPool, attack, defence, pos, tileString, combat);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public void LevelUp(){
        super.LevelUp();
        this.currentEnergy = 100;
        SetAttackPoints(attackPoints + (3 * playerLevel));
    }
    //game tick is missing
    @Override
    public int Cast(List<Unit> listOfUnits) {
        if(currentEnergy < cost) {
            throw new IllegalArgumentException("No enough energy for cast.");
        }
        currentEnergy -= cost;
        for (Unit other :listOfUnits ) {
            if (!(listOfUnits == null || listOfUnits.size() == 0)) {
                this.Attack(other);
            }
        }
        return 0;
    }

    public boolean Cast(Enemy enemy) {
        return this.combatUtiles.Attack(enemy, attackPoints, "Rogue");
    }
    public void GameTick() {
        currentEnergy = Math.min(currentEnergy + 10, 100);
    }

    @Override
    public String Description() {
        return String.format("%s\t\tEnergy: %d/100",
                super.Description(), this.currentEnergy);
    }
    @Override
    public int GetRange() {
        return attackRange;
    }
}