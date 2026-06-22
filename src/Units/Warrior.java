package Units;
import Combat_System.CombatSystem;
import Game.Position;
import SpecialAbility.SpecialAbility;
import SpecialAbility.AvengersShiled;

public class Warrior extends Player {
    protected final Integer attackRange;
    protected SpecialAbility specialA;
    protected Integer remainingCoolDown;
    protected Integer abilityCoolDown;
    public Warrior(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, int abilityCoolDown) {
        super(name, healthPool, attack, defence, pos,combat);
        remainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
        specialA = new AvengersShiled();
        attackRange = specialA.GetRange();
    }

    @Override
    public int Cast() {
        if(remainingCoolDown == 0) {
            int res = specialA.Cast();
            this.remainingCoolDown = this.abilityCoolDown;
            SetHealthAmount( healthAmount + 10 * this.defencePoints);
            return res;
        }
        else {
            throw new IllegalArgumentException("cannot cast because there is cooldown remain");
        }
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
    public String Description() {
        return "place holder for Warrior description";
    }
}
