package Units;
import Combat_System.CombatSystem;
import Game.Position;

import java.util.List;

public abstract class Player extends Unit {
    protected Integer experience;
    protected Integer playerLevel;

    public Player(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat){
        super(name, healthPool, healthPool, attack, defence, pos, combat);
        experience = 0;
        playerLevel = 1;

    }

    public abstract int Cast(List<Unit> listOfUnits);

    public abstract String Description();


    public boolean AdvanceAccept(Unit other) {
        return other.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit other) {
        return other.AttackVisit(this);
    }
    public boolean AdvanceVisit(Enemy enemy) {
        int res = this.combatUtiles.Combat(this, enemy);
        if (res == -1) {
            System.out.println("Place holder for player is dead.");
            return false;
        }
        System.out.println("place holder for player is alive and gained points? or 0 points");
        return true;
    }
    public boolean AttackVisit(Enemy enemy) {
        this.Cast(enemy);
    }

    public abstract boolean Cast(Enemy enemy);

    public void LevelUp(){
        SetExperience(experience - 50 * playerLevel);
        playerLevel++;
        SetHealthPool(healthPool + 10 * playerLevel);
        SetHealthAmount(healthPool);
        SetAttackPoints(attackPoints + 4 * playerLevel);
        SetDefencePoints(defencePoints + playerLevel);
    }

    public void SetExperience(int i) {
        experience = Math.max(i, 0);
        if(experience >= 50 * playerLevel) {
            LevelUp();
        }
    }
    public boolean isDead(){
        return healthAmount <= 0;
    }
    public abstract int GetRange();
    @Override
    public String description() {
        // 50 * level calculates the current experience threshold for the next level up [cite: 105]
        return super.description() + String.format("\t\tLevel: %d\t\tExperience: %d/%d",
                playerLevel,
                experience,
                50 * playerLevel);
    }
}
