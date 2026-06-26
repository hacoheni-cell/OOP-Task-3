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

    public Warrior(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, String tileString, int abilityCoolDown, MessageCallback messageCallback) {
        super(name, healthPool, attack, defence, pos, tileString, combat, messageCallback);
        remainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        if(remainingCoolDown == 0) {
            listOfUnits.remove(this);
            messageCallback.send(this.getName() + " used Avenger's Strike, healing for " + (10 * this.defencePoints) + ".");
            if (listOfUnits == null || listOfUnits.isEmpty()) {
                messageCallback.send("There are no enemies in range.");
                return 0;
            }
            Random rand = new Random();
            Unit target = listOfUnits.get(rand.nextInt(listOfUnits.size()));
            this.Attack(target);

            this.remainingCoolDown = this.abilityCoolDown;
            SetHealthAmount(Math.min(healthPool, healthAmount + 10 * this.defencePoints));
            return 1;
        }
        else {
            messageCallback.send("Ability is on cooldown: " + remainingCoolDown + " turns remaining.");
            return 0;
        }
    }

    @Override
    public boolean Cast(Enemy enemy) {
        double damage = healthAmount * 0.1;
        boolean hit = this.combatUtiles.Attack(enemy, damage, "Warrior");

        if (enemy.isDead()) {
            messageCallback.send(enemy.getName() + " died. " + this.getName() + " gained " + enemy.getExperience() + " experience.");
            this.SetExperience(this.experience + enemy.getExperience());
        }
        return hit;
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