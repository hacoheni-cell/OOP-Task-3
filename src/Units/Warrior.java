package Units;
import Game.Position;

public class Warrior extends Player {
    protected Integer reamainingCoolDown;
    protected Integer abilityCoolDown;
    public Warrior(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat,int abilityCoolDown) {
        super(name, healthPool, attack, defence, pos,specialAbility,combat);
        reamainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
    }

    @Override
    public int Cast() {
        if(reamainingCoolDown == 0) {
            //int res = this.specialAbility.cast();?
            this.reamainingCoolDown = this.abilityCoolDown;
            SetHealthAmount(Math.min(healthAmount + ( 10 * this.defencePoints),this.healthPool));
            //- Randomly hits one enemy within range < 3 for an amount equals to 10% of the
            // warrior’s health pool
        }
       // else {throw new IllegalArgumentException("cannot cast because there is cooldown remain");}
        return 0;
    }
    public boolean decracseCoolDown() {
        if (this.reamainingCoolDown > 0) {
            reamainingCoolDown--;
            return true;
        }
        return false;
    }

    //game tick is missing
    public void LevelUp() {
        PlayerLevelUp();
        this.reamainingCoolDown = 0;
        Unit.SetHealthPool( healthPool + 5 * playerLevel);
        Unit.SetDefencePoints(defencePoints + playerLevel);
        //playerLevel++; in player?
    }

    @Override
    public String Description() {
        return "place holder for Warrior description";
    }
}
