package Units;
import Game.Position;
import SpecialAbility.SpecialAbility;
import SpecialAbility.FanOfKnives;

public class Rouge extends Player {
    protected final Integer Range;
    protected SpecialAbility specialA;
    protected Integer cost;
    protected Integer currentEnergy;
    public Rouge(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat, Integer cost) {
        super(name, healthPool, attack, defence, pos,combat);
        this.cost = cost;
        this.currentEnergy = 100;
        specialA = new FanOfKnives();
        Range = specialAbility.GetRange();
    }

    public void LevelUp(){
        super.LevelUp();
        this.currentEnergy = 100;
        SetAttackPoints(attackPoints + (3 * playerLevel));
    }
    //game tick is missing
    @Override
    public int Cast() {
        if(currentEnergy < cost) {
            throw new IllegalArgumentException("No enough energy for cast.");
        }
        currentEnergy -= cost;
        //- For each enemy within range < 2, deal damage (reduce health value) equals to the
        //rogue’s attack points (each enemy will try to defend itself).
        return 0;
    }

    @Override
    public String Description() {
        return "place holder for Rouge description";
    }
    @Override
    public int GetRange() {
        return Range;
    }
}
