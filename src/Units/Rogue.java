package Units;

import Combat_System.CombatSystem;
import Game.Position;
import Game.MessageCallback;
import java.util.List;

public class Rogue extends Player {
    protected final Integer attackRange = 2;
    protected Integer cost;
    protected Integer currentEnergy;

    public Rogue(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, String tileString, Integer cost, MessageCallback messageCallback) {
        super(name, healthPool, attack, defence, pos, tileString, combat, messageCallback);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public void LevelUp(){
        super.LevelUp();
        this.currentEnergy = 100;
        SetAttackPoints(attackPoints + (3 * playerLevel));
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        if(currentEnergy < cost) {
            messageCallback.send("Not enough energy for cast.");
            return 0;
        }
        currentEnergy -= cost;
        for (Unit other : listOfUnits) {
            this.Attack(other);
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