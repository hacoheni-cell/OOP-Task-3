package Units;
import Combat_System.CombatSystem;
import Game.Position;
import java.util.List;
import java.util.Random;

public class Warrior extends Player {
    protected final Integer attackRange = 3;
    protected Integer remainingCoolDown;
    protected Integer abilityCoolDown;
    public Warrior(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, int abilityCoolDown) {
        super(name, healthPool, attack, defence, pos,combat);
        remainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        if(remainingCoolDown == 0) {
            if (listOfUnits == null || listOfUnits.size() == 0) {
                return 0;
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(listOfUnits.size());
            Unit otherUnit = listOfUnits.get(randomIndex);
            this.Attack(otherUnit);
            this.remainingCoolDown = this.abilityCoolDown;
            SetHealthAmount( healthAmount + 10 * this.defencePoints);
            return 1;
        }
        else {
            throw new IllegalArgumentException("cannot cast because there is cooldown remain");
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