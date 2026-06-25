package Units;

import Combat_System.CombatSystem;
import Game.Position;
import Game.MessageCallback;
import java.util.List;
import java.util.Random;

public class Warrior extends Player {
    protected final Integer attackRange = 3;
    protected Integer remainingCoolDown;
    protected Integer abilityCoolDown;

    public Warrior(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, char tileString, int abilityCoolDown, MessageCallback messageCallback) {
        super(name, healthPool, attack, defence, pos, tileString, combat, messageCallback);
        remainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        if(remainingCoolDown == 0) {
            if (listOfUnits == null || listOfUnits.size() == 0) {
                messageCallback.send("There are no enemies in range.");
                return 0;
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(listOfUnits.size());
            Unit otherUnit = listOfUnits.get(randomIndex);
            this.Attack(otherUnit);
            this.remainingCoolDown = this.abilityCoolDown;
            SetHealthAmount(Math.min(healthPool, healthAmount + 10 * this.defencePoints));
            return 1;
        }
        else {
            messageCallback.send("Ability is on cooldown: " + remainingCoolDown + " turns remaining.");
            return 0;
        }
    }

    public boolean Cast(Enemy enemy) {
        double damage = healthAmount * (double)0.1;
        return this.combatUtiles.Attack(enemy, damage, "Warrior");
    }

    public boolean decreaseCoolDown() {
        if (this.remainingCoolDown > 0) {
            remainingCoolDown--;
            return true;
        }
        return false;
    }

    public void LevelUp() {
        super.LevelUp();
        this.remainingCoolDown = 0;
        SetHealthPool( healthPool + 5 * playerLevel);
        SetDefencePoints(defencePoints + playerLevel);
    }

    @Override
    public int GetRange() {
        return attackRange;
    }

    @Override
    public void GameTick() {
        this.decreaseCoolDown();
    }

    @Override
    public String Description() {
        return String.format("%s\t\tCooldown: %d/%d",
                super.Description(), this.remainingCoolDown, this.abilityCoolDown);
    }
}