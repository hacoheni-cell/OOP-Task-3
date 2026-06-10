package Units;
import Game.Position;
public class Warrior extends Player {
    Integer reamainingCoolDown;
    Integer abilityCoolDown;
    public Warrior(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat,int abilityCoolDown) {
        super(name, healthPool, attack, defence, pos,specialAbility,combat);
        reamainingCoolDown = 0;
        this.abilityCoolDown = abilityCoolDown;
    }

    @Override
    public int Cast() {
        if (reamainingCoolDown == 0) {
            int res = this.specialAbility.cast();
            this.reamainingCoolDown = this.abilityCoolDown;
            Unit.setHealthAmount(Math.min(healthAmount + ( 10 * this.defencePoints),this.healthPool));
            //hit random enemy, implement in here or in ability?
        }
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
        this.reamainingCoolDown = 0;
        Unit.setHealthPool( healthPool + 5 * playerLevel);
         Unit.setDefencePoints(defencePoints + playerLevel);
        //Player.LevelUp();? need to do it or it automaticly?
        //playerLevel++; in player?
    }

    @Override
    public String Description() {
        return "place holder for Warrior description";
    }
}
