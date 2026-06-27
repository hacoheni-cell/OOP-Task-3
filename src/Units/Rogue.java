package Units;

import combat.CombatSystem;
import Game.Position;
import businessLayer.MessageCallback;
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
        messageCallback.send(this.getName() + " cast Fan of Knives.");

        listOfUnits.remove(this);

        for (Unit target : listOfUnits) {
            // Visitor!
            this.Attack(target);
        }
        return 0;
    }

    @Override
    public boolean Cast(Enemy enemy) {
        boolean hit = this.combatUtiles.Attack(enemy, attackPoints, "Rogue");

        if (enemy.isDead()) {
            messageCallback.send(enemy.getName() + " died. " + this.getName() + " gained " + enemy.getExperience() + " experience.");
            this.SetExperience(this.experience + enemy.getExperience());
        }
        return hit;
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